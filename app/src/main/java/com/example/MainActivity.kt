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
import com.example.util.PermissionHelper
import com.example.viewmodel.NeuroSyncViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NeuroSyncViewModel by viewModels()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        PermissionHelper.setNotificationsGranted(this, isGranted)
        viewModel.refreshPermissions()
        launchBackgroundServiceSafely()
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        results[Manifest.permission.RECORD_AUDIO]?.let {
            PermissionHelper.setMicGranted(this, it)
        }
        results[Manifest.permission.CAMERA]?.let {
            PermissionHelper.setCameraGranted(this, it)
            if (it) {
                viewModel.startCameraGazeTracking(this)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            results[Manifest.permission.POST_NOTIFICATIONS]?.let {
                PermissionHelper.setNotificationsGranted(this, it)
            }
        }
        viewModel.refreshPermissions()
        launchBackgroundServiceSafely()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                NeuroSyncApp(
                    viewModel = viewModel,
                    onRequestMasterPermissions = {
                        requestAllCorePermissions()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            viewModel.refreshPermissions()
        } catch (e: Throwable) {
            Log.e("MainActivity", "onResume refresh error", e)
        }
    }

    fun requestAllCorePermissions() {
        val permsToRequest = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permsToRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permsToRequest.add(Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (permsToRequest.isNotEmpty()) {
            requestMultiplePermissions.launch(permsToRequest.toTypedArray())
        } else {
            PermissionHelper.setMicGranted(this, true)
            PermissionHelper.setCameraGranted(this, true)
            PermissionHelper.setNotificationsGranted(this, true)
            viewModel.refreshPermissions()
            launchBackgroundServiceSafely()
        }
    }

    private fun checkAndLaunchBackgroundService() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    PermissionHelper.setNotificationsGranted(this, true)
                    launchBackgroundServiceSafely()
                }
            } else {
                PermissionHelper.setNotificationsGranted(this, true)
                launchBackgroundServiceSafely()
            }
        } catch (e: Throwable) {
            Log.e("MainActivity", "Permission check safe ignore", e)
            launchBackgroundServiceSafely()
        }
    }

    private fun launchBackgroundServiceSafely() {
        try {
            com.example.receiver.BootReceiver.startNeuralContextService(this)
            com.example.receiver.BootReceiver.schedulePerpetualWatchdog(this)
        } catch (e: Throwable) {
            Log.e("MainActivity", "Safe fallback for background service", e)
        }
    }
}
