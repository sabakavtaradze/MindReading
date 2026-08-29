package com.example.util

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.content.ContextCompat

object PermissionHelper {

    private const val PREFS_NAME = "neurosync_permission_prefs"
    private const val KEY_MIC_GRANTED = "key_mic_granted"
    private const val KEY_CAMERA_GRANTED = "key_camera_granted"
    private const val KEY_NOTIF_GRANTED = "key_notif_granted"
    private const val KEY_USAGE_GRANTED = "key_usage_granted"
    private const val KEY_ACCESSIBILITY_GRANTED = "key_accessibility_granted"
    private const val KEY_OVERLAY_GRANTED = "key_overlay_granted"
    private const val KEY_BATTERY_OPTIMIZATION_IGNORED = "key_battery_opt_ignored"
    private const val KEY_MASTER_SYNC_ENABLED = "key_master_sync_enabled"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isMicGranted(context: Context): Boolean {
        return try {
            val systemGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            val prefGranted = getPrefs(context).getBoolean(KEY_MIC_GRANTED, false)
            systemGranted || prefGranted
        } catch (e: Throwable) {
            false
        }
    }

    fun setMicGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_MIC_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isCameraGranted(context: Context): Boolean {
        return try {
            val systemGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            val prefGranted = getPrefs(context).getBoolean(KEY_CAMERA_GRANTED, false)
            systemGranted || prefGranted
        } catch (e: Throwable) {
            false
        }
    }

    fun setCameraGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_CAMERA_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isNotificationsGranted(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val systemGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                val prefGranted = getPrefs(context).getBoolean(KEY_NOTIF_GRANTED, true)
                systemGranted || prefGranted
            } else {
                true
            }
        } catch (e: Throwable) {
            true
        }
    }

    fun setNotificationsGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_NOTIF_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isUsageStatsGranted(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            val systemGranted = mode == AppOpsManager.MODE_ALLOWED
            val prefGranted = getPrefs(context).getBoolean(KEY_USAGE_GRANTED, false)
            systemGranted || prefGranted
        } catch (e: Throwable) {
            try { getPrefs(context).getBoolean(KEY_USAGE_GRANTED, false) } catch (t: Throwable) { false }
        }
    }

    fun setUsageStatsGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_USAGE_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isAccessibilityGranted(context: Context): Boolean {
        return try {
            val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager ?: return false
            val enabledServices = am.getEnabledAccessibilityServiceList(android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            val systemGranted = enabledServices.any { it.resolveInfo.serviceInfo.packageName == context.packageName }
            val prefGranted = getPrefs(context).getBoolean(KEY_ACCESSIBILITY_GRANTED, false)
            systemGranted || prefGranted
        } catch (e: Throwable) {
            try { getPrefs(context).getBoolean(KEY_ACCESSIBILITY_GRANTED, false) } catch (t: Throwable) { false }
        }
    }

    fun setAccessibilityGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_ACCESSIBILITY_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isOverlayGranted(context: Context): Boolean {
        return try {
            val systemGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else {
                true
            }
            val prefGranted = getPrefs(context).getBoolean(KEY_OVERLAY_GRANTED, false)
            systemGranted || prefGranted
        } catch (e: Throwable) {
            try { getPrefs(context).getBoolean(KEY_OVERLAY_GRANTED, false) } catch (t: Throwable) { false }
        }
    }

    fun setOverlayGranted(context: Context, granted: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_OVERLAY_GRANTED, granted).apply()
        } catch (e: Throwable) {}
    }

    fun isBatteryOptimizationIgnored(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
                val systemIgnored = powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true
                val prefIgnored = getPrefs(context).getBoolean(KEY_BATTERY_OPTIMIZATION_IGNORED, false)
                systemIgnored || prefIgnored
            } else {
                true
            }
        } catch (e: Throwable) {
            getPrefs(context).getBoolean(KEY_BATTERY_OPTIMIZATION_IGNORED, false)
        }
    }

    fun setBatteryOptimizationIgnored(context: Context, ignored: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_BATTERY_OPTIMIZATION_IGNORED, ignored).apply()
        } catch (e: Throwable) {}
    }

    fun requestIgnoreBatteryOptimization(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        } catch (e: Throwable) {
            try {
                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (t: Throwable) {
                openAppSettings(context)
            }
        }
    }

    fun isMasterSyncEnabled(context: Context): Boolean {
        return try {
            getPrefs(context).getBoolean(KEY_MASTER_SYNC_ENABLED, true)
        } catch (e: Throwable) {
            true
        }
    }

    fun setMasterSyncEnabled(context: Context, enabled: Boolean) {
        try {
            getPrefs(context).edit().putBoolean(KEY_MASTER_SYNC_ENABLED, enabled).apply()
        } catch (e: Throwable) {}
    }

    fun openUsageAccessSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Throwable) {
            openAppSettings(context)
        }
    }

    fun openAccessibilitySettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Throwable) {
            openAppSettings(context)
        }
    }

    fun openOverlaySettings(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } else {
                openAppSettings(context)
            }
        } catch (e: Throwable) {
            openAppSettings(context)
        }
    }

    fun openSamsungBackgroundUsageLimits(context: Context) {
        val samsungIntents = listOf(
            Intent().setClassName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity"),
            Intent().setClassName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"),
            Intent("com.samsung.android.sm.ACTION_BATTERY"),
            Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
            Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        )

        for (intent in samsungIntents) {
            try {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                return
            } catch (e: Throwable) {
                // try next
            }
        }
        openAppSettings(context)
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
