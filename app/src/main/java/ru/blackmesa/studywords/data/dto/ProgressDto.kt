package ru.blackmesa.studywords.data.dto

data class ProgressDto(
    val wordId: Int,
    val wordUserId: Int,
    val status: Int,
    val version: Long,
    val repeatDate: Long,
)

