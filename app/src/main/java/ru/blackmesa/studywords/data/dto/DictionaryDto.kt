package ru.blackmesa.studywords.data.dto

data class DictionaryDto(
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