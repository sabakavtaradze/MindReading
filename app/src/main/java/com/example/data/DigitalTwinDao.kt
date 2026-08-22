package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DigitalTwinDao {
    @Query("SELECT * FROM digital_twin_checkpoints ORDER BY dayNumber DESC, savedTimestamp DESC")
    fun getAllCheckpoints(): Flow<List<DigitalTwinCheckpointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckpoint(checkpoint: DigitalTwinCheckpointEntity)

    @Query("DELETE FROM digital_twin_checkpoints WHERE id = :id")
    suspend fun deleteCheckpointById(id: Long)

    @Query("DELETE FROM digital_twin_checkpoints")
    suspend fun deleteAll()
}
