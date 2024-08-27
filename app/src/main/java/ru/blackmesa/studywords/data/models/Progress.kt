package ru.blackmesa.studywords.data.models

data class Progress(
    val wordId: Int,
    val status: Int,
    val repeatDate: Long,
    val version: Long,
    val touched: Boolean,
)

