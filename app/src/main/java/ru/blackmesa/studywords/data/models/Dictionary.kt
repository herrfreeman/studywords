package ru.blackmesa.studywords.data.models

data class Dictionary(
    val id: Int,
    val name: String,
    val orderField: Int,
    val version: Long,
    val parentId: Int,
    val isFolder: Boolean,
    val isDefault: Boolean,
    val downloaded: Boolean,
)