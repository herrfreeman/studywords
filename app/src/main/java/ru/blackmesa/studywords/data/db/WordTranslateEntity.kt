package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordtranslate_table")
data class WordTranslateEntity(
    @PrimaryKey
    val id: Int,
    val wordid: Int,
    val userid: Int,
    val translate: String,
)

