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
import com.example.service.NeuralContextService
import com.example.ui.NeuroSyncApp
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NeuroSyncViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NeuroSyncViewModel by viewModels()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startNeuralBackgroundService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safety handler to prevent fatal unhandled crashes
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("NeuroSync", "Handled uncaught exception on thread ${thread.name}", throwable)
        }

        enableEdgeToEdge()

        checkNotificationPermissionAndStartService()

        setContent {
            MyApplicationTheme {
                NeuroSyncApp(viewModel = viewModel)
            }
        }
    }

    private fun checkNotificationPermissionAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                startNeuralBackgroundService()
            } else {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startNeuralBackgroundService()
        }
    }

    private fun startNeuralBackgroundService() {
        try {
            val serviceIntent = Intent(this, NeuralContextService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, serviceIntent)
            } else {
                startService(serviceIntent)
            }
        } catch (e: Throwable) {
            Log.e("MainActivity", "Could not start NeuralContextService in background", e)
        }
    }
}
