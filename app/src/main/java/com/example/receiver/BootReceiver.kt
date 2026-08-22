package com.example.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.service.NeuralContextService

/**
 * 24/7 Perpetual Background Watchdog & Boot Receiver
 * Guarantees NeuroSync runs continuously for months, surviving reboots, app updates, and memory pressure.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        val action = intent?.action
        Log.d("BootReceiver", "Received action for perpetual background watchdog: $action")

        // Start the Foreground Service
        startNeuralContextService(context)

        // Schedule the next Watchdog Alarm to keep service perpetually alive
        schedulePerpetualWatchdog(context)
    }

    companion object {
        const val ACTION_WATCHDOG = "com.example.neurosync.ACTION_WATCHDOG_ALARM"
        private const val WATCHDOG_INTERVAL_MS = 60 * 60 * 1000L // 1 hour recurrent watchdog

        fun startNeuralContextService(context: Context) {
            try {
                val serviceIntent = Intent(context, NeuralContextService::class.java).apply {
                    putExtra(NeuralContextService.EXTRA_NOTIFICATION_TEXT, "უწყვეტი ფონური ნეირო-სინქრონიზაცია აქტიურია (24/7)")
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        ContextCompat.startForegroundService(context, serviceIntent)
                    } catch (e: Throwable) {
                        Log.w("BootReceiver", "Foreground service start deferred or not allowed currently: ${e.message}")
                    }
                } else {
                    context.startService(serviceIntent)
                }
            } catch (e: Throwable) {
                Log.w("BootReceiver", "Could not start NeuralContextService", e)
            }
        }

        fun schedulePerpetualWatchdog(context: Context) {
            try {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
                val intent = Intent(context, BootReceiver::class.java).apply {
                    action = ACTION_WATCHDOG
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    9026,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val triggerAt = System.currentTimeMillis() + WATCHDOG_INTERVAL_MS
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
                }
            } catch (e: Throwable) {
                Log.e("BootReceiver", "Error scheduling perpetual watchdog alarm", e)
            }
        }
    }
}
