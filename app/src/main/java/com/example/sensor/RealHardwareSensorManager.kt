package com.example.sensor

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs
import kotlin.math.sqrt

data class PhysicalSensorInfo(
    val name: String,
    val typeName: String,
    val vendor: String,
    val powerMa: Float,
    val resolution: Float,
    val isLive: Boolean = true
)

data class RealHardwareSensorState(
    val isAvailable: Boolean = false,
    val isTracking: Boolean = false,
    // Accelerometer & Tremor
    val accelX: Float = 0f,
    val accelY: Float = 0f,
    val accelZ: Float = 9.8f,
    val gyroX: Float = 0f,
    val gyroY: Float = 0f,
    val gyroZ: Float = 0f,
    val microTremorMagnitude: Float = 0.04f,
    val physiologicalTremorHz: Float = 9.4f,
    val neuromuscularStabilityPct: Int = 92,
    val handTensionLevel: String = "Relaxed • Low Neuro-Motor Strain",
    // Magnetometer & Digital Compass
    val magX: Float = 0f,
    val magY: Float = 0f,
    val magZ: Float = 0f,
    val compassHeadingDeg: Float = 42.0f,
    val compassCardinal: String = "ჩრდილო-აღმოსავლეთი (NE)",
    val pitchDeg: Float = -4.5f,
    val rollDeg: Float = 2.1f,
    // Ambient Light Sensor
    val ambientLightLux: Float = 340f,
    val lightCondition: String = "ოთახის ოპტიმალური განათება",
    // Proximity Sensor
    val proximityDistanceCm: Float = 5.0f,
    val isNearEarOrFace: Boolean = false,
    // Barometer / Atmospheric Pressure
    val atmosphericPressureHpa: Float = 1013.25f,
    val estimatedAltitudeMeters: Float = 480f,
    // Motion, Gravity & Steps
    val gravityX: Float = 0f,
    val gravityY: Float = 0f,
    val gravityZ: Float = 9.8f,
    val linearAccelX: Float = 0f,
    val linearAccelY: Float = 0f,
    val linearAccelZ: Float = 0f,
    val totalStepsDetected: Int = 1420,
    val isUserMoving: Boolean = false,
    // Environmental Temperature & Humidity
    val ambientTemperatureC: Float = 22.5f,
    val relativeHumidityPct: Float = 45f,
    // Device Battery & Thermal
    val batteryPct: Int = 88,
    val isCharging: Boolean = false,
    val batteryVoltageMv: Int = 4120,
    val batteryHealth: String = "Good (ჯანსაღი)",
    val batteryTemperatureCelsius: Float = 28.5f,
    // System Memory (RAM)
    val totalRamMb: Long = 8192L,
    val availRamMb: Long = 4320L,
    val ramUsagePct: Int = 47,
    // Physical Sensor Inventory
    val hasPhysicalAccelerometer: Boolean = false,
    val hasPhysicalGyroscope: Boolean = false,
    val hasPhysicalMagnetometer: Boolean = false,
    val hasPhysicalLightSensor: Boolean = false,
    val hasPhysicalProximitySensor: Boolean = false,
    val hasPhysicalBarometer: Boolean = false,
    val hasPhysicalStepDetector: Boolean = false,
    val totalActiveHardwareSensors: Int = 0,
    val detectedSensorList: List<PhysicalSensorInfo> = emptyList()
)

class RealHardwareSensorManager(private val context: Context) : SensorEventListener {

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

    private val magnetometer: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    } catch (e: Throwable) {
        null
    }

    private val linearAccel: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    } catch (e: Throwable) {
        null
    }

    private val gravitySensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
    } catch (e: Throwable) {
        null
    }

    private val rotationVectorSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    } catch (e: Throwable) {
        null
    }

    private val lightSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
    } catch (e: Throwable) {
        null
    }

    private val proximitySensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    } catch (e: Throwable) {
        null
    }

    private val pressureSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_PRESSURE)
    } catch (e: Throwable) {
        null
    }

    private val stepDetectorSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    } catch (e: Throwable) {
        null
    }

    private val stepCounterSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    } catch (e: Throwable) {
        null
    }

    private val tempSensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    } catch (e: Throwable) {
        null
    }

    private val humiditySensor: Sensor? = try {
        sensorManager?.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    } catch (e: Throwable) {
        null
    }

    private var batteryReceiver: BroadcastReceiver? = null

    private val lastAccelerometerValues = FloatArray(3)
    private val lastMagnetometerValues = FloatArray(3)
    private var hasAccelSample = false
    private var hasMagSample = false

    private val accelHistory = FloatArray(16)
    private var historyIndex = 0
    private var lastEmittedTimestamp = 0L

    private val physicalSensorsList: List<PhysicalSensorInfo> = try {
        sensorManager?.getSensorList(Sensor.TYPE_ALL)?.map { sensor ->
            PhysicalSensorInfo(
                name = sensor.name,
                typeName = sensor.stringType ?: "sensor.type.${sensor.type}",
                vendor = sensor.vendor,
                powerMa = sensor.power,
                resolution = sensor.resolution,
                isLive = true
            )
        } ?: emptyList()
    } catch (e: Throwable) {
        emptyList()
    }

    private val _sensorState = MutableStateFlow(
        RealHardwareSensorState(
            isAvailable = accelerometer != null || lightSensor != null || magnetometer != null,
            hasPhysicalAccelerometer = accelerometer != null,
            hasPhysicalGyroscope = gyroscope != null,
            hasPhysicalMagnetometer = magnetometer != null,
            hasPhysicalLightSensor = lightSensor != null,
            hasPhysicalProximitySensor = proximitySensor != null,
            hasPhysicalBarometer = pressureSensor != null,
            hasPhysicalStepDetector = stepDetectorSensor != null || stepCounterSensor != null,
            totalActiveHardwareSensors = listOfNotNull(
                accelerometer, gyroscope, magnetometer, linearAccel,
                gravitySensor, rotationVectorSensor, lightSensor,
                proximitySensor, pressureSensor, stepDetectorSensor
            ).size,
            detectedSensorList = physicalSensorsList
        )
    )
    val sensorState: StateFlow<RealHardwareSensorState> = _sensorState.asStateFlow()

    fun startListening() {
        if (sensorManager == null) return
        try {
            val delay = SensorManager.SENSOR_DELAY_UI
            accelerometer?.let { sensorManager.registerListener(this, it, delay) }
            gyroscope?.let { sensorManager.registerListener(this, it, delay) }
            magnetometer?.let { sensorManager.registerListener(this, it, delay) }
            linearAccel?.let { sensorManager.registerListener(this, it, delay) }
            gravitySensor?.let { sensorManager.registerListener(this, it, delay) }
            rotationVectorSensor?.let { sensorManager.registerListener(this, it, delay) }
            lightSensor?.let { sensorManager.registerListener(this, it, delay) }
            proximitySensor?.let { sensorManager.registerListener(this, it, delay) }
            pressureSensor?.let { sensorManager.registerListener(this, it, delay) }
            stepDetectorSensor?.let { sensorManager.registerListener(this, it, delay) }
            stepCounterSensor?.let { sensorManager.registerListener(this, it, delay) }
            tempSensor?.let { sensorManager.registerListener(this, it, delay) }
            humiditySensor?.let { sensorManager.registerListener(this, it, delay) }

            registerBatteryMonitor()
            updateSystemMemoryMetrics()

            _sensorState.value = _sensorState.value.copy(
                isTracking = true,
                hasPhysicalAccelerometer = accelerometer != null,
                hasPhysicalGyroscope = gyroscope != null,
                hasPhysicalMagnetometer = magnetometer != null,
                hasPhysicalLightSensor = lightSensor != null,
                hasPhysicalProximitySensor = proximitySensor != null,
                hasPhysicalBarometer = pressureSensor != null,
                hasPhysicalStepDetector = stepDetectorSensor != null || stepCounterSensor != null,
                totalActiveHardwareSensors = listOfNotNull(
                    accelerometer, gyroscope, magnetometer, linearAccel,
                    gravitySensor, rotationVectorSensor, lightSensor,
                    proximitySensor, pressureSensor, stepDetectorSensor
                ).size
            )
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error registering sensor listeners", e)
        }
    }

    private fun registerBatteryMonitor() {
        try {
            if (batteryReceiver == null) {
                batteryReceiver = object : BroadcastReceiver() {
                    override fun onReceive(c: Context?, intent: Intent?) {
                        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                            val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
                            val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4100)
                            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)

                            val pct = if (level >= 0 && scale > 0) ((level.toFloat() / scale) * 100).toInt() else 85
                            val charging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
                            val tempC = temp / 10f

                            val healthStr = when (health) {
                                BatteryManager.BATTERY_HEALTH_GOOD -> "Good (ჯანსაღი)"
                                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat (გადახურებული)"
                                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead (დამჯდარი)"
                                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage (ზედმეტი ძაბვა)"
                                else -> "Normal (ნორმალური)"
                            }

                            _sensorState.value = _sensorState.value.copy(
                                batteryPct = pct,
                                isCharging = charging,
                                batteryVoltageMv = voltage,
                                batteryHealth = healthStr,
                                batteryTemperatureCelsius = if (tempC > 0) tempC else 28.5f
                            )
                        }
                    }
                }
                val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                context.registerReceiver(batteryReceiver, filter)
            }
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Battery receiver registration error", e)
        }
    }

    private fun updateSystemMemoryMetrics() {
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)

            val totalMb = memInfo.totalMem / (1024 * 1024)
            val availMb = memInfo.availMem / (1024 * 1024)
            val usedMb = totalMb - availMb
            val usedPct = if (totalMb > 0) ((usedMb.toFloat() / totalMb) * 100).toInt() else 45

            _sensorState.value = _sensorState.value.copy(
                totalRamMb = totalMb,
                availRamMb = availMb,
                ramUsagePct = usedPct
            )
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Memory info error", e)
        }
    }

    fun stopListening() {
        try {
            sensorManager?.unregisterListener(this)
            batteryReceiver?.let {
                try {
                    context.unregisterReceiver(it)
                } catch (ignored: Throwable) {}
                batteryReceiver = null
            }
            _sensorState.value = _sensorState.value.copy(isTracking = false)
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error unregistering sensor listeners", e)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val now = System.currentTimeMillis()
        if (now - lastEmittedTimestamp < 200L) {
            return
        }
        lastEmittedTimestamp = now

        try {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = event.values.getOrNull(0) ?: 0f
                    val y = event.values.getOrNull(1) ?: 0f
                    val z = event.values.getOrNull(2) ?: 9.8f

                    lastAccelerometerValues[0] = x
                    lastAccelerometerValues[1] = y
                    lastAccelerometerValues[2] = z
                    hasAccelSample = true

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

                    updateOrientationIfPossible()

                    _sensorState.value = _sensorState.value.copy(
                        accelX = x,
                        accelY = y,
                        accelZ = z,
                        microTremorMagnitude = avgTremor,
                        physiologicalTremorHz = tremorHz,
                        neuromuscularStabilityPct = stability,
                        handTensionLevel = tensionStatus,
                        isUserMoving = diffFromGravity > 0.4f
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
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    val mx = event.values.getOrNull(0) ?: 0f
                    val my = event.values.getOrNull(1) ?: 0f
                    val mz = event.values.getOrNull(2) ?: 0f

                    lastMagnetometerValues[0] = mx
                    lastMagnetometerValues[1] = my
                    lastMagnetometerValues[2] = mz
                    hasMagSample = true

                    updateOrientationIfPossible()

                    _sensorState.value = _sensorState.value.copy(
                        magX = mx,
                        magY = my,
                        magZ = mz
                    )
                }
                Sensor.TYPE_ROTATION_VECTOR -> {
                    val rotationMatrix = FloatArray(9)
                    val orientationAngles = FloatArray(3)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)

                    val azimuthDeg = (Math.toDegrees(orientationAngles[0].toDouble()).toFloat() + 360f) % 360f
                    val pitchDeg = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                    val rollDeg = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

                    val cardinal = getCardinalDirection(azimuthDeg)

                    _sensorState.value = _sensorState.value.copy(
                        compassHeadingDeg = (azimuthDeg * 10).toInt() / 10f,
                        compassCardinal = cardinal,
                        pitchDeg = (pitchDeg * 10).toInt() / 10f,
                        rollDeg = (rollDeg * 10).toInt() / 10f
                    )
                }
                Sensor.TYPE_LIGHT -> {
                    val lux = event.values.getOrNull(0) ?: 340f
                    val condition = when {
                        lux < 10f -> "ძალიან ბნელი ოთახი / ღამე"
                        lux < 60f -> "დაბალი განათება / ბინდი"
                        lux < 400f -> "კომფორტული ოთახის განათება"
                        lux < 1200f -> "ნათელი ოფისი / სამუშაო მაგიდა"
                        else -> "კაშკაშა მზის შუქი"
                    }
                    _sensorState.value = _sensorState.value.copy(
                        ambientLightLux = lux,
                        lightCondition = condition
                    )
                }
                Sensor.TYPE_PROXIMITY -> {
                    val dist = event.values.getOrNull(0) ?: 5f
                    val maxRange = event.sensor.maximumRange
                    val isNear = dist < 2.0f || (maxRange > 0 && dist < maxRange / 2f)
                    _sensorState.value = _sensorState.value.copy(
                        proximityDistanceCm = dist,
                        isNearEarOrFace = isNear
                    )
                }
                Sensor.TYPE_PRESSURE -> {
                    val pressure = event.values.getOrNull(0) ?: 1013.25f
                    val altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
                    _sensorState.value = _sensorState.value.copy(
                        atmosphericPressureHpa = (pressure * 10).toInt() / 10f,
                        estimatedAltitudeMeters = (altitude * 10).toInt() / 10f
                    )
                }
                Sensor.TYPE_GRAVITY -> {
                    _sensorState.value = _sensorState.value.copy(
                        gravityX = event.values.getOrNull(0) ?: 0f,
                        gravityY = event.values.getOrNull(1) ?: 0f,
                        gravityZ = event.values.getOrNull(2) ?: 9.8f
                    )
                }
                Sensor.TYPE_LINEAR_ACCELERATION -> {
                    _sensorState.value = _sensorState.value.copy(
                        linearAccelX = event.values.getOrNull(0) ?: 0f,
                        linearAccelY = event.values.getOrNull(1) ?: 0f,
                        linearAccelZ = event.values.getOrNull(2) ?: 0f
                    )
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    _sensorState.value = _sensorState.value.copy(
                        totalStepsDetected = _sensorState.value.totalStepsDetected + 1,
                        isUserMoving = true
                    )
                }
                Sensor.TYPE_STEP_COUNTER -> {
                    val steps = event.values.getOrNull(0)?.toInt() ?: _sensorState.value.totalStepsDetected
                    _sensorState.value = _sensorState.value.copy(
                        totalStepsDetected = steps,
                        isUserMoving = true
                    )
                }
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    val temp = event.values.getOrNull(0) ?: 22.5f
                    _sensorState.value = _sensorState.value.copy(ambientTemperatureC = temp)
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    val hum = event.values.getOrNull(0) ?: 45f
                    _sensorState.value = _sensorState.value.copy(relativeHumidityPct = hum)
                }
            }
        } catch (e: Throwable) {
            Log.e("RealHardwareSensorManager", "Error processing onSensorChanged", e)
        }
    }

    private fun updateOrientationIfPossible() {
        if (hasAccelSample && hasMagSample) {
            val rotationMatrix = FloatArray(9)
            val inclinationMatrix = FloatArray(9)
            val success = SensorManager.getRotationMatrix(
                rotationMatrix,
                inclinationMatrix,
                lastAccelerometerValues,
                lastMagnetometerValues
            )
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val azimuthDeg = (Math.toDegrees(orientation[0].toDouble()).toFloat() + 360f) % 360f
                val pitchDeg = Math.toDegrees(orientation[1].toDouble()).toFloat()
                val rollDeg = Math.toDegrees(orientation[2].toDouble()).toFloat()

                val cardinal = getCardinalDirection(azimuthDeg)

                _sensorState.value = _sensorState.value.copy(
                    compassHeadingDeg = (azimuthDeg * 10).toInt() / 10f,
                    compassCardinal = cardinal,
                    pitchDeg = (pitchDeg * 10).toInt() / 10f,
                    rollDeg = (rollDeg * 10).toInt() / 10f
                )
            }
        }
    }

    private fun getCardinalDirection(degrees: Float): String {
        return when {
            degrees >= 337.5 || degrees < 22.5 -> "ჩრდილოეთი (N • 0°)"
            degrees in 22.5..67.5 -> "ჩრდილო-აღმოსავლეთი (NE)"
            degrees in 67.5..112.5 -> "აღმოსავლეთი (E • 90°)"
            degrees in 112.5..157.5 -> "სამხრეთ-აღმოსავლეთი (SE)"
            degrees in 157.5..202.5 -> "სამხრეთი (S • 180°)"
            degrees in 202.5..247.5 -> "სამხრეთ-დასავლეთი (SW)"
            degrees in 247.5..292.5 -> "დასავლეთი (W • 270°)"
            else -> "ჩრდილო-დასავლეთი (NW)"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}

