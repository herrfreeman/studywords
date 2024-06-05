package ru.blackmesa.studywords.data.dto

data class ProgressDto(
    val wordid: Int,
    val status: Int,
    val repeatdate: Long,
    val version: Long,
)

