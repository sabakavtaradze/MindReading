package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs
import kotlin.math.sqrt

data class RealHardwareSensorState(
    val isAvailable: Boolean = false,
    val isTracking: Boolean = false,
    val accelX: Float = 0f,
    val accelY: Float = 0f,
    val accelZ: Float = 9.8f,
    val gyroX: Float = 0f,
    val gyroY: Float = 0f,
    val gyroZ: Float = 0f,
    val microTremorMagnitude: Float = 0.04f, // 0.01 - 0.50
    val physiologicalTremorHz: Float = 9.4f, // typical human resting hand tremor: 8-12 Hz
    val neuromuscularStabilityPct: Int = 92,
    val handTensionLevel: String = "Relaxed • Low Neuro-Motor Strain"
)

class RealHardwareSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val linearAccel: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private val _sensorState = MutableStateFlow(
        RealHardwareSensorState(isAvailable = accelerometer != null)
    )
    val sensorState: StateFlow<RealHardwareSensorState> = _sensorState.asStateFlow()

    private var lastTimestamp: Long = 0
    private var accelHistory = FloatArray(16)
    private var historyIndex = 0

    fun startListening() {
        if (sensorManager == null) return
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        linearAccel?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        _sensorState.value = _sensorState.value.copy(isTracking = true)
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
        _sensorState.value = _sensorState.value.copy(isTracking = false)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val rawMagnitude = sqrt(x * x + y * y + z * z)
                val diffFromGravity = abs(rawMagnitude - 9.80665f)

                accelHistory[historyIndex % accelHistory.size] = diffFromGravity
                historyIndex++

                val avgTremor = accelHistory.average().toFloat().coerceIn(0.01f, 1.2f)
                val stability = (100 - (avgTremor * 120).toInt()).coerceIn(40, 99)
                val tremorHz = (8.5f + (avgTremor * 5f)).coerceIn(8.0f, 13.5f)

                val tensionStatus = when {
                    stability > 85 -> "Relaxed • Low Neuro-Motor Strain"
                    stability > 65 -> "Mild Motor Tension • Active Attention"
                    else -> "High Motor Tremor • Anticipatory Excitement / Fatigue"
                }

                _sensorState.value = _sensorState.value.copy(
                    accelX = x,
                    accelY = y,
                    accelZ = z,
                    microTremorMagnitude = avgTremor,
                    physiologicalTremorHz = tremorHz,
                    neuromuscularStabilityPct = stability,
                    handTensionLevel = tensionStatus
                )
            }
            Sensor.TYPE_GYROSCOPE -> {
                val gx = event.values[0]
                val gy = event.values[1]
                val gz = event.values[2]
                _sensorState.value = _sensorState.value.copy(
                    gyroX = gx,
                    gyroY = gy,
                    gyroZ = gz
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
