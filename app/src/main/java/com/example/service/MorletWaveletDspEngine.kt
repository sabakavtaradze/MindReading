package com.example.service

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Continuous Morlet Wavelet Transform (CWT) & Scalogram Engine
 * Performs localized time-frequency analysis for sub-vocal micro-bursts and neural rhythms.
 * Morlet Wavelet: psi(t) = pi^(-1/4) * exp(i * w0 * t) * exp(-t^2 / 2)
 */
object MorletWaveletDspEngine {

    private const val W0 = 6.0 // Standard central frequency for Morlet wavelet
    private val SCALES = floatArrayOf(2.0f, 4.0f, 8.0f, 16.0f, 32.0f, 64.0f)

    data class WaveletScalogramResult(
        val scalogramMatrix: Array<FloatArray>,
        val peakEnergyScale: Float,
        val peakEnergyFrequencyHz: Float,
        val microBurstDetected: Boolean,
        val neuralGammaAlphaRatio: Float
    )

    /**
     * Compute Continuous Wavelet Transform on a 1D biosignal or audio window
     */
    fun computeMorletScalogram(
        signal: FloatArray,
        samplingRateHz: Float = 1600.0f
    ): WaveletScalogramResult {
        val n = minOf(signal.size, 128)
        if (n == 0) {
            return WaveletScalogramResult(
                scalogramMatrix = emptyArray(),
                peakEnergyScale = 0f,
                peakEnergyFrequencyHz = 0f,
                microBurstDetected = false,
                neuralGammaAlphaRatio = 1.0f
            )
        }

        val numScales = SCALES.size
        val scalogram = Array(numScales) { FloatArray(n) }
        var maxCoeff = 0.0f
        var bestScale = SCALES[0]

        var alphaEnergy = 0.0f // 8 - 12 Hz
        var gammaEnergy = 0.0f // 30 - 80 Hz

        for (sIdx in 0 until numScales) {
            val scale = SCALES[sIdx]
            val pseudoFreqHz = (W0 * samplingRateHz) / (2.0 * PI * scale).toFloat()

            for (t in 0 until n) {
                var realSum = 0.0
                var imagSum = 0.0

                val halfWindow = minOf(16, n / 2)
                for (tau in -halfWindow..halfWindow) {
                    val idx = t + tau
                    if (idx in 0 until n) {
                        val dt = tau / scale
                        val envelope = exp(-0.5 * dt * dt)
                        val angle = W0 * dt
                        val morletReal = envelope * cos(angle)
                        val morletImag = envelope * sin(angle)

                        val sigVal = signal[idx]
                        realSum += sigVal * morletReal
                        imagSum += sigVal * morletImag
                    }
                }

                val coeff = sqrt((realSum * realSum + imagSum * imagSum) / scale).toFloat()
                scalogram[sIdx][t] = coeff

                if (pseudoFreqHz in 8.0f..13.0f) alphaEnergy += coeff
                if (pseudoFreqHz in 30.0f..80.0f) gammaEnergy += coeff

                if (coeff > maxCoeff) {
                    maxCoeff = coeff
                    bestScale = scale
                }
            }
        }

        val peakFreq = ((W0 * samplingRateHz) / (2.0 * PI * bestScale)).toFloat()
        val isBurst = maxCoeff > 0.65f
        val gammaAlphaRatio = (gammaEnergy + 0.01f) / (alphaEnergy + 0.01f)

        return WaveletScalogramResult(
            scalogramMatrix = scalogram,
            peakEnergyScale = bestScale,
            peakEnergyFrequencyHz = peakFreq,
            microBurstDetected = isBurst,
            neuralGammaAlphaRatio = gammaAlphaRatio.coerceIn(0.2f, 4.5f)
        )
    }
}
