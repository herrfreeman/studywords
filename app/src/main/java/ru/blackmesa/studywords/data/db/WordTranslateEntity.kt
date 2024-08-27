package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordtranslate_table")
data class WordTranslateEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "wordid")
    val wordId: Int,
    @ColumnInfo(name = "userid")
    val userId: Int,
    val translate: String,
)

