package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "predictions")
data class PredictionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val summary: String,
    val matchConfidence: Float,
    val touchActivityLevel: String,
    val audioSpectrumDb: String,
    val visualContext: String,
    val neuralSyncRate: String,
    val actionPlan: String,
    val timestamp: Long = System.currentTimeMillis()
)
