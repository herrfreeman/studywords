package ru.blackmesa.studywords.data.dto

data class WordTranslateDto(
    val id: Int,
    val wordid: Int,
    val translate: String,
    val version: Long,
)

