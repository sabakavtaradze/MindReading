package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log10

class NeuralContextService : Service() {

    private val binder = LocalBinder()
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    private var isRecordingAudio = false
    private var audioRecord: AudioRecord? = null

    private val _micDecibels = MutableStateFlow(0f)
    val micDecibels: StateFlow<Float> = _micDecibels.asStateFlow()

    private val _touchRateHz = MutableStateFlow(1.2f)
    val touchRateHz: StateFlow<Float> = _touchRateHz.asStateFlow()

    private val _activeContextName = MutableStateFlow("Developer IDE & Code Analysis")
    val activeContextName: StateFlow<String> = _activeContextName.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): NeuralContextService = this@NeuralContextService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundServiceWithNotification()
        startAudioSampling()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NeuroSync Telemetry Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Active neural context tracking and intent prediction service"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundServiceWithNotification() {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("NeuroSync Neural Link")
            .setContentText("Context Telemetry Active • 98.4% Synaptic Match")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .build()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAudioSampling() {
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

                        while (isRecordingAudio) {
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
                            delay(100)
                        }
                    }
                }
            } catch (e: SecurityException) {
                // Permission not granted
            } catch (e: Exception) {
                e.printStackTrace()
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
        isRecordingAudio = false
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val CHANNEL_ID = "neurosync_channel"
        const val NOTIFICATION_ID = 1001
    }
}
