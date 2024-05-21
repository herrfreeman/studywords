package ru.blackmesa.studywords.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict_table")
data class DictEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val orderfield: Int,
    val version: Long,
)

