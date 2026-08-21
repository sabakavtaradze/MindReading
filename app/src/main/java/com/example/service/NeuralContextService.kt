package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class NeuralContextService : Service() {

    private val binder = LocalBinder()
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    private var isRunning = true
    private var wakeLock: PowerManager.WakeLock? = null

    private val _micDecibels = MutableStateFlow(28.4f)
    val micDecibels: StateFlow<Float> = _micDecibels.asStateFlow()

    private val _touchRateHz = MutableStateFlow(1.8f)
    val touchRateHz: StateFlow<Float> = _touchRateHz.asStateFlow()

    private val _activeContextName = MutableStateFlow("ფონური ნეირო-მონიტორინგი")
    val activeContextName: StateFlow<String> = _activeContextName.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): NeuralContextService = this@NeuralContextService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        try {
            createNotificationChannel()
            startForegroundServiceWithNotification()
            acquireWakeLock()
            startBackgroundMonitoringLoop()
        } catch (e: Throwable) {
            Log.e("NeuralContextService", "Safe onCreate initialization exception", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            startForegroundServiceWithNotification()
        } catch (e: Throwable) {
            Log.e("NeuralContextService", "onStartCommand exception", e)
        }
        return START_STICKY
    }

    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
            wakeLock = powerManager?.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "NeuroSync:BackgroundTelemetryWakeLock"
            )?.apply {
                setReferenceCounted(false)
                acquire(15 * 60 * 1000L) // 15 min safe window
            }
        } catch (e: Throwable) {
            Log.e("NeuralContextService", "WakeLock error", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "NeuroSync ნეირონული კავშირი",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "აზრების მუდმივი პროგნოზირება და ფონური სენსორული ტელემეტრია"
                    setShowBadge(false)
                    enableVibration(false)
                    enableLights(false)
                }
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                manager?.createNotificationChannel(channel)
            } catch (e: Throwable) {
                Log.e("NeuralContextService", "NotificationChannel error", e)
            }
        }
    }

    private fun startForegroundServiceWithNotification() {
        try {
            val notificationIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("NeuroSync • ნეირონული კავშირი აქტიურია")
                .setContentText("აზრების პროგნოზირება მუშაობს ფონურ რეჟიმში")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Throwable) {
            Log.e("NeuralContextService", "Foreground service start error", e)
        }
    }

    private fun startBackgroundMonitoringLoop() {
        scope.launch {
            while (isRunning) {
                try {
                    delay(3000)
                    _micDecibels.value = 24f + Random.nextFloat() * 8f
                } catch (e: Throwable) {
                    // Safe loop catch
                }
            }
        }
    }

    fun updateTouchActivity(tapsCount: Int) {
        _touchRateHz.value = (tapsCount * 0.8f + 1.2f).coerceAtMost(12f)
    }

    fun setContextName(name: String) {
        _activeContextName.value = name
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        try {
            scope.cancel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {
        const val CHANNEL_ID = "neurosync_telemetry_channel"
        const val NOTIFICATION_ID = 2026
    }
}
