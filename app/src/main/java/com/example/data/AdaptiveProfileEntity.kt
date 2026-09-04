package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adaptive_personal_profile")
data class AdaptiveProfileEntity(
    @PrimaryKey val id: Int = 1,
    val baselineHeartRateBpm: Float = 72.0f,
    val baselinePupilDiameterMm: Float = 3.45f,
    val baselineReactionLatencyMs: Int = 320,
    val baselineFocusLevel: Float = 0.85f,
    val baselineEmotionalValence: Float = 0.65f,
    val system1RatioPct: Int = 42,
    val system2RatioPct: Int = 58,
    val egoDepletionRecoveryRate: Float = 0.92f,
    val personalAdaptationScorePct: Int = 96,
    val totalLifetimeInferences: Long = 0L,
    val totalExperiencePoints: Long = 100L,
    val dominantCircadianPeak: String = "დილის კოგნიტური პიკი (Peak Flow Zone)",
    val circadianChronotype: String = "Deep Flow Chronotype (Adaptive)",
    val lastCalibratedTimestampMs: Long = System.currentTimeMillis()
)
