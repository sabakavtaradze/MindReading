package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
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
    val microTremorMagnitude: Float = 0.04f,
    val physiologicalTremorHz: Float = 9.4f,
    val neuromuscularStabilityPct: Int = 92,
    val handTensionLevel: String = "Relaxed • Low Neuro-Motor Strain"
)

class RealHardwareSensorManager(context: Context) : SensorEventListener {

    private val sensorManager = try {
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    } catch (e: Throwable) {
        null
    }
    private val accelerometer: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    } catch (e: Throwable) {
        null
    }
    private val gyroscope: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    } catch (e: Throwable) {
        null
    }
    private val linearAccel: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    } catch (e: Throwable) {
        null
    }

    private val _sensorState = MutableStateFlow(
        RealHardwareSensorState(isAvailable = accelerometer != null)
    )
    val sensorState: StateFlow<RealHardwareSensorState> = _sensorState.asStateFlow()

    private val accelHistory = FloatArray(16)
    private var historyIndex = 0
    private var lastEmittedTimestamp = 0L

    fun startListening() {
        if (sensorManager == null) return
        try {
            accelerometer?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            gyroscope?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            linearAccel?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            _sensorState.value = _sensorState.value.copy(isTracking = true)
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error registering sensor listeners", e)
        }
    }

    fun stopListening() {
        try {
            sensorManager?.unregisterListener(this)
            _sensorState.value = _sensorState.value.copy(isTracking = false)
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error unregistering sensor listeners", e)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val now = System.currentTimeMillis()
        if (now - lastEmittedTimestamp < 500L) {
            return
        }
        lastEmittedTimestamp = now

        try {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = event.values.getOrNull(0) ?: 0f
                    val y = event.values.getOrNull(1) ?: 0f
                    val z = event.values.getOrNull(2) ?: 9.8f

                    val rawMagnitude = sqrt(x * x + y * y + z * z)
                    val diffFromGravity = abs(rawMagnitude - 9.80665f)

                    synchronized(accelHistory) {
                        historyIndex = (historyIndex + 1) % accelHistory.size
                        accelHistory[historyIndex] = diffFromGravity
                    }

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
                    val gx = event.values.getOrNull(0) ?: 0f
                    val gy = event.values.getOrNull(1) ?: 0f
                    val gz = event.values.getOrNull(2) ?: 0f
                    _sensorState.value = _sensorState.value.copy(
                        gyroX = gx,
                        gyroY = gy,
                        gyroZ = gz
                    )
                }
            }
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error processing onSensorChanged", e)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
