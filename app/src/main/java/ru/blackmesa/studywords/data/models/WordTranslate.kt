package ru.blackmesa.studywords.data.models

data class WordTranslate(
    val id: Int,
    val wordid: Int,
    val translate: String,
    val version: Long,
)

