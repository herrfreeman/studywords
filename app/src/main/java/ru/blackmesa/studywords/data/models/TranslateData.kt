package ru.blackmesa.studywords.data.models

data class TranslateData(
    val wordid: Int,
    val id: Int,
    val translate: String,
    var priority: Int,
)