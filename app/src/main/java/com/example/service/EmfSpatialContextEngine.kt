package com.example.service

import kotlin.math.sqrt
import kotlin.random.Random

data class EmfSpatialContextMetrics(
    val magneticFluxDensityMicroTesla: Float = 48.5f, // ~30-60 µT Earth baseline, >75 µT indicates electronics/PC/workstation proximity
    val estimatedEnvironmentDomain: String = "სამუშაო სამუშაო სადგური (Workstation / Dev Space)",
    val proximityToElectronicsScore: Float = 0.88f,
    val altitudeDeltaMeters: Float = 0.4f,
    val ambientRssiDensity: Int = 8, // Nearby RF/Bluetooth signals
    val spatialCognitiveDomainConstraint: String = "DEV_ACTION & ლოგიკური ანალიზი",
    val emfAnomalyDetected: Boolean = false,
    val georgianSpatialSummary: String = "მონიტორისა და ელექტრონიკის EMF ველი მიუთითებს სამუშაო გარემოზე."
)

class EmfSpatialContextEngine {

    private val spatialProfiles = listOf(
        Triple("სამუშაო სადგური (Workstation & Monitor)", 82.0f, "DEV_ACTION & ლოგიკური ანალიზი"),
        Triple("მობილური სივრცე / მოძრაობა (Transit)", 45.0f, "URGENT_COMMAND & სწრაფი შეტყობინება"),
        Triple("სოციალური გარემო (Social / Coffee Space)", 52.0f, "SOCIAL_MESSAGE & კომუნიკაცია"),
        Triple("დასვენების ზონა (Quiet Alpha Space)", 38.0f, "REST_STATE & მოდუნება")
    )

    private var profileIndex = 0

    fun computeSpatialContext(
        magX: Float = 12.0f,
        magY: Float = 24.0f,
        magZ: Float = 40.0f,
        pressureHpa: Float = 1013.25f,
        lightLux: Float = 320f
    ): EmfSpatialContextMetrics {
        val totalMag = sqrt((magX * magX + magY * magY + magZ * magZ).toDouble()).toFloat()
        val (envName, baseEmf, domainConstraint) = spatialProfiles[profileIndex % spatialProfiles.size]

        val finalEmf = totalMag.coerceAtLeast(25f) + (baseEmf - 50f) * 0.4f + Random.nextFloat() * 4f
        val proximity = ((finalEmf - 35f) / 50f).coerceIn(0.1f, 1.0f)
        val isAnomaly = finalEmf > 95f

        val summary = when {
            finalEmf > 70f -> "მაღალი EMF ველი ($finalEmf µT) — ახლოსაა კომპიუტერის ეკრანთან და ელექტრონიკასთან."
            lightLux < 40f -> "დაბალი განათება ($lightLux Lux) — მშვიდი განტვირთვის ან ღამის რეჟიმი."
            else -> "სტანდარტული გეო-სივრცითი ველი ($finalEmf µT). დომენი: $envName."
        }

        return EmfSpatialContextMetrics(
            magneticFluxDensityMicroTesla = finalEmf,
            estimatedEnvironmentDomain = envName,
            proximityToElectronicsScore = proximity,
            altitudeDeltaMeters = (pressureHpa - 1013.25f) * -8.3f,
            ambientRssiDensity = 6 + Random.nextInt(7),
            spatialCognitiveDomainConstraint = domainConstraint,
            emfAnomalyDetected = isAnomaly,
            georgianSpatialSummary = summary
        )
    }

    fun stepNextSpatialProfile(): EmfSpatialContextMetrics {
        profileIndex++
        return computeSpatialContext()
    }
}
