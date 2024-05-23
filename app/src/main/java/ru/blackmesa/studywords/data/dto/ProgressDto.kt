package ru.blackmesa.studywords.data.dto

import androidx.room.Entity

data class ProgressDto(
    val wordid: Int,
    val userid: Int,
    val status: Int,
    val answerdate: Long,
    val version: Long,
)

