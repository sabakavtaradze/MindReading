package com.example.data

import kotlinx.coroutines.flow.Flow

class DigitalTwinRepository(private val digitalTwinDao: DigitalTwinDao) {
    val allCheckpoints: Flow<List<DigitalTwinCheckpointEntity>> = digitalTwinDao.getAllCheckpoints()

    suspend fun insertCheckpoint(checkpoint: DigitalTwinCheckpointEntity) {
        digitalTwinDao.insertCheckpoint(checkpoint)
    }

    suspend fun deleteCheckpointById(id: Long) {
        digitalTwinDao.deleteCheckpointById(id)
    }

    suspend fun clearAll() {
        digitalTwinDao.deleteAll()
    }
}
