package com.example.service

/**
 * Multivariate Kalman Filter & Sensor Drift Compensator
 * Filters noise and sensor jitter from 3D Kinematics (accelerometer, gyro, eye-gaze tracking).
 * State vector x = [pos, vel]^T, with process covariance Q and measurement covariance R.
 */
class MultivariateKalmanFilter(
    private val processNoiseQ: Float = 0.008f,
    private val measurementNoiseR: Float = 0.05f
) {
    // 1D Kalman state
    class Kalman1D(private val q: Float, private val r: Float) {
        var x: Float = 0.0f  // Estimated state
        var p: Float = 1.0f  // Estimation error covariance
        var k: Float = 0.0f  // Kalman gain

        fun update(measurement: Float): Float {
            // 1. Predict
            p += q

            // 2. Update
            k = p / (p + r)
            x += k * (measurement - x)
            p *= (1.0f - k)
            return x
        }
    }

    private val filterGazeX = Kalman1D(processNoiseQ, measurementNoiseR)
    private val filterGazeY = Kalman1D(processNoiseQ, measurementNoiseR)
    private val filterAccX = Kalman1D(processNoiseQ * 2f, measurementNoiseR * 1.5f)
    private val filterAccY = Kalman1D(processNoiseQ * 2f, measurementNoiseR * 1.5f)
    private val filterAccZ = Kalman1D(processNoiseQ * 2f, measurementNoiseR * 1.5f)

    data class FilteredKinematics(
        val smoothGazeX: Float,
        val smoothGazeY: Float,
        val smoothAccX: Float,
        val smoothAccY: Float,
        val smoothAccZ: Float,
        val estimatedDriftMagnitude: Float
    )

    fun filter(
        rawGazeX: Float,
        rawGazeY: Float,
        rawAccX: Float,
        rawAccY: Float,
        rawAccZ: Float
    ): FilteredKinematics {
        val sGazeX = filterGazeX.update(rawGazeX)
        val sGazeY = filterGazeY.update(rawGazeY)
        val sAccX = filterAccX.update(rawAccX)
        val sAccY = filterAccY.update(rawAccY)
        val sAccZ = filterAccZ.update(rawAccZ)

        val drift = kotlin.math.abs(rawGazeX - sGazeX) + kotlin.math.abs(rawGazeY - sGazeY)

        return FilteredKinematics(
            smoothGazeX = sGazeX,
            smoothGazeY = sGazeY,
            smoothAccX = sAccX,
            smoothAccY = sAccY,
            smoothAccZ = sAccZ,
            estimatedDriftMagnitude = drift
        )
    }

    companion object {
        val shared = MultivariateKalmanFilter()
    }
}
