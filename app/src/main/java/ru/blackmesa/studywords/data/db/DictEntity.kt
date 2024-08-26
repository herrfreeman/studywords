package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict_table", primaryKeys = ["id", "userid"])
data class DictEntity(
    val id: Int,
    val userid: Int,
    val name: String,
    val orderfield: Int,
    val version: Long,
    val isfolder: Boolean,
    val parentid: Int,
    val parentuserid: Int,
    val isdefault: Boolean,
)

