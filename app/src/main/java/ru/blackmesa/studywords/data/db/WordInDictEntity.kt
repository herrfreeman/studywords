package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordindict_table", primaryKeys = ["wordid", "dictid"])
data class WordInDictEntity(
    val wordid: Int,
    val dictid: Int,
    val version: Long,
)

