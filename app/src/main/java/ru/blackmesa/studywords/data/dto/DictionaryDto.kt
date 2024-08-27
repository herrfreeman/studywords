package ru.blackmesa.studywords.data.dto

data class DictionaryDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val orderField: Int,
    val version: Long,
    val parentId: Int,
    val parentUserId: Int,
    val isFolder: Boolean,
    val isDefault: Boolean,
)