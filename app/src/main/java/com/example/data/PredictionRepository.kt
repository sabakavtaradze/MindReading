package com.example.data

import kotlinx.coroutines.flow.Flow

class PredictionRepository(private val predictionDao: PredictionDao) {
    val allPredictions: Flow<List<PredictionEntity>> = predictionDao.getAllPredictions()

    suspend fun insert(prediction: PredictionEntity) {
        predictionDao.insertPrediction(prediction)
    }

    suspend fun deleteById(id: Long) {
        predictionDao.deletePredictionById(id)
    }

    suspend fun clearAll() {
        predictionDao.deleteAll()
    }
}
