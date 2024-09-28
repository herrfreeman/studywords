package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "priority_translate_table", primaryKeys = ["wordid", "dictid"])
data class PriorityTranslateEntity(
    @ColumnInfo(name = "dictid")
    val dictId: Int,
    @ColumnInfo(name = "wordid")
    val wordId: Int,
    @ColumnInfo(name = "translateid")
    val translateId: Int,
    val count: Int,
)

