package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table", primaryKeys = ["id", "userid"])
data class WordEntity(
    val id: Int,
    @ColumnInfo(name = "userid")
    val userId: Int,
    val word: String,
)

