package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "progress_table", primaryKeys = ["wordid", "userid"])
data class ProgressEntity(
    @ColumnInfo(name = "wordid")
    val wordId: Int,
    @ColumnInfo(name = "userid")
    val userId: Int,
    val status: Int,
    @ColumnInfo(name = "repeatdate")
    val repeatDate: Long,
    val version: Long,
    var touched: Boolean,
)

