package com.example

import android.app.Application
import android.util.Log

class NeuroSyncApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Global crash guard to prevent Samsung Device Care force-close dialogs
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("NeuroSyncApp", "Intercepted fatal exception on ${thread.name}", throwable)
            // Log cleanly without letting system show crash popup
            try {
                // If it's a critical error on main thread that cannot be recovered, pass to default
                if (throwable is OutOfMemoryError) {
                    defaultHandler?.uncaughtException(thread, throwable)
                }
            } catch (e: Throwable) {
                // Prevent cascade
            }
        }
    }
}
