package ru.blackmesa.studywords.data.models

import androidx.room.Entity

data class Progress(
    val wordid: Int,
    val status: Int,
    val answerdate: Long,
    val version: Long,
)

