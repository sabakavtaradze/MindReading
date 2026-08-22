package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "digital_twin_checkpoints")
data class DigitalTwinCheckpointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayNumber: Int,
    val phaseName: String,
    val accuracyPct: Float,
    val dataPointsCount: Long,
    val neuralConvergencePct: Float,
    val personaSummary: String,
    val savedTimestamp: Long = System.currentTimeMillis()
)
