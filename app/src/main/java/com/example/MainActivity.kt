package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.service.NeuralContextService
import com.example.ui.NeuroSyncApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NeuroSyncViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: NeuroSyncViewModel by viewModels()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchBackgroundServiceSafely()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                NeuroSyncApp(viewModel = viewModel)
            }
        }

        // Safely check notification permission and launch background service only when app is RESUMED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                delay(800)
                checkNotificationPermissionAndStartService()
            }
        }
    }

    private fun checkNotificationPermissionAndStartService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    launchBackgroundServiceSafely()
                } else {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                launchBackgroundServiceSafely()
            }
        } catch (e: Throwable) {
            Log.e("MainActivity", "Notification permission check error", e)
        }
    }

    private fun launchBackgroundServiceSafely() {
        try {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                val serviceIntent = Intent(this, NeuralContextService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(this, serviceIntent)
                } else {
                    startService(serviceIntent)
                }
            }
        } catch (e: Throwable) {
            Log.e("MainActivity", "Safe fallback for background service start", e)
        }
    }
}
