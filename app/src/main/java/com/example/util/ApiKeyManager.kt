package com.example.util

import android.content.Context
import android.content.SharedPreferences

object ApiKeyManager {
    private const val PREFS_NAME = "neurosync_api_prefs"
    private const val KEY_CUSTOM_GEMINI_KEY = "custom_gemini_api_key"

    fun getActiveGeminiApiKey(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val customKey = prefs.getString(KEY_CUSTOM_GEMINI_KEY, "")?.trim().orEmpty()
        if (customKey.isNotBlank()) {
            return customKey
        }

        val buildConfigKey = try {
            val field = com.example.BuildConfig::class.java.getField("GEMINI_API_KEY")
            field.get(null) as? String ?: ""
        } catch (e: Throwable) {
            ""
        }.trim()

        if (buildConfigKey.isNotBlank() && buildConfigKey != "MY_GEMINI_API_KEY") {
            return buildConfigKey
        }

        return ""
    }

    fun saveCustomApiKey(context: Context, key: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_GEMINI_KEY, key.trim()).apply()
    }

    fun hasValidApiKey(context: Context): Boolean {
        return getActiveGeminiApiKey(context).isNotBlank()
    }
}
