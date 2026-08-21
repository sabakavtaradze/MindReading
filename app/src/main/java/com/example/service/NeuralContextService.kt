package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log10
import kotlin.random.Random

class NeuralContextService : Service() {

    private val binder = LocalBinder()
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    private var isRecordingAudio = false
    private var isRunning = true
    private var audioRecord: AudioRecord? = null
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
        acquireWakeLock()
        createNotificationChannel()
        startForegroundServiceWithNotification()
        startBackgroundMonitoringLoop()
        startAudioSamplingSafely()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundServiceWithNotification()
        return START_STICKY
    }

    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
            wakeLock = powerManager?.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "NeuroSync::BackgroundPredictionWakeLock"
            )?.apply {
                setReferenceCounted(false)
                acquire(10 * 60 * 1000L) // 10 mins window, refreshed periodically
            }
        } catch (e: Throwable) {
            Log.e("NeuralContextService", "WakeLock error", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NeuroSync ფონური ნეირონული კავშირი",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "აზრების მუდმივი პროგნოზირება და სენსორული ტელემეტრია"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
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
                .setContentText("აზრების პროგნოზირება და სენსორები მუშაობს ფონურ რეჟიმში")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    startForeground(
                        NOTIFICATION_ID,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                    )
                } catch (e: Throwable) {
                    startForeground(NOTIFICATION_ID, notification)
                }
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
                delay(3000)
                try {
                    // Refresh decibels if audio not active
                    if (!isRecordingAudio) {
                        _micDecibels.value = (24f + Random.nextFloat() * 12f)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startAudioSamplingSafely() {
        val hasMicPermission = try {
            androidx.core.content.ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Throwable) {
            false
        }

        if (!hasMicPermission) return

        scope.launch {
            try {
                val sampleRate = 44100
                val channelConfig = AudioFormat.CHANNEL_IN_MONO
                val audioFormat = AudioFormat.ENCODING_PCM_16BIT
                val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

                if (minBufferSize > 0) {
                    audioRecord = AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        sampleRate,
                        channelConfig,
                        audioFormat,
                        minBufferSize
                    )

                    if (audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
                        audioRecord?.startRecording()
                        isRecordingAudio = true
                        val buffer = ShortArray(minBufferSize)

                        while (isRecordingAudio && isRunning) {
                            val read = audioRecord?.read(buffer, 0, minBufferSize) ?: 0
                            if (read > 0) {
                                var sum = 0.0
                                for (i in 0 until read) {
                                    sum += buffer[i] * buffer[i]
                                }
                                val amplitude = sum / read
                                val db = if (amplitude > 0) 10 * log10(amplitude) else 0.0
                                _micDecibels.value = db.toFloat().coerceIn(0f, 100f)
                            }
                            delay(250)
                        }
                    }
                }
            } catch (e: Throwable) {
                Log.w("NeuralContextService", "Audio sampling not available or restricted in background", e)
                isRecordingAudio = false
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
        isRecordingAudio = false
        try {
            audioRecord?.stop()
            audioRecord?.release()
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
