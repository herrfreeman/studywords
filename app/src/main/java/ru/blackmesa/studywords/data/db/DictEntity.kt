package ru.blackmesa.studywords.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dict_table")
data class DictEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "orderfield")
    val orderField: Int,
    val version: Long,
    @ColumnInfo(name = "parentid")
    val parentId: Int,
    @ColumnInfo(name = "isfolder")
    val isFolder: Boolean,
    @ColumnInfo(name = "isdefault")
    val isDefault: Boolean,
    @ColumnInfo(name = "downloaded")
    val downloaded: Boolean,
)

