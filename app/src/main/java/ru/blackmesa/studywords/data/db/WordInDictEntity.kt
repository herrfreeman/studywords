package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wordindict_table", primaryKeys = ["wordid", "dictid"])
data class WordInDictEntity(
    @ColumnInfo(name = "wordid")
    val wordId: Int,
    @ColumnInfo(name = "dictid")
    val dictId: Int,
)

