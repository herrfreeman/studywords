package ru.blackmesa.studywords.data.dto

data class DictionaryDto(
    val id: Int,
    val name: String,
    val orderField: Int,
    val version: Long,
    val parentId: Int,
    val isFolder: Boolean,
    val isDefault: Boolean,
)