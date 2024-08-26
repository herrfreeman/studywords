package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordindict_table", primaryKeys = ["wordid", "userid", "dictid"])
data class WordInDictEntity(
    val wordid: Int,
    val userid: Int,
    val dictid: Int,
)

