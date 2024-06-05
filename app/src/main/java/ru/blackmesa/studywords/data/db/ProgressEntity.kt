package ru.blackmesa.studywords.data.db

import androidx.room.Entity

@Entity(tableName = "progress_table", primaryKeys = ["wordid", "userid"])
data class ProgressEntity(
    val wordid: Int,
    val userid: Int,
    val status: Int,
    val repeatdate: Long,
    val version: Long,
    val touched: Boolean,
)

