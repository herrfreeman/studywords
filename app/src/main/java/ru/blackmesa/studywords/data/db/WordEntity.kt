package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class WordEntity(
    @PrimaryKey
    val id: Int,
    val word: String,
    val deleted: Boolean,
)

