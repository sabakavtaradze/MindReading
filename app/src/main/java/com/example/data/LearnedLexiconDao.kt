package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnedLexiconDao {
    @Query("SELECT * FROM learned_lexicon_words ORDER BY addedTimestampMs DESC")
    fun getAllWords(): Flow<List<LearnedLexiconWordEntity>>

    @Query("SELECT * FROM learned_lexicon_words ORDER BY addedTimestampMs DESC")
    suspend fun getAllWordsDirect(): List<LearnedLexiconWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: LearnedLexiconWordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<LearnedLexiconWordEntity>)

    @Query("SELECT COUNT(*) FROM learned_lexicon_words")
    suspend fun getWordCount(): Int

    @Query("DELETE FROM learned_lexicon_words WHERE word = :word")
    suspend fun deleteWord(word: String)
}
