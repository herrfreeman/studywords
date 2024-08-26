package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table", primaryKeys = ["id", "userid"])
data class WordEntity(
    val id: Int,
    val userid: Int,
    val word: String,
)

