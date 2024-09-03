package ru.blackmesa.studywords.data.dto

data class WordTranslateDto(
    val id: Int,
    val wordId: Int,
    val translate: String,
    val priority: Int,
)

