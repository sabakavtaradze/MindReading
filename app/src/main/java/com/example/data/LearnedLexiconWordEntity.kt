package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learned_lexicon_words")
data class LearnedLexiconWordEntity(
    @PrimaryKey val word: String,
    val category: String = "AI_LEARNED",
    val originSource: String = "GEMINI_CLOUD_AI",
    val usageFrequency: Int = 1,
    val definition: String = "ავტონომიურად ნასწავლი ცნება",
    val synonyms: String = "",
    val addedTimestampMs: Long = System.currentTimeMillis()
)
