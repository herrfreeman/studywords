package ru.blackmesa.studywords.data.models

data class Progress(
    val wordid: Int,
    val status: Int,
    val repeatdate: Long,
    val version: Long,
    val touched: Boolean,
)

