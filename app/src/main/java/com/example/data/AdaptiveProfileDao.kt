package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdaptiveProfileDao {
    @Query("SELECT * FROM adaptive_personal_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<AdaptiveProfileEntity?>

    @Query("SELECT * FROM adaptive_personal_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileDirect(): AdaptiveProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: AdaptiveProfileEntity)
}
