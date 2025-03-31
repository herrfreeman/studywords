package ru.blackmesa.studywords.data.models

data class WordAndTranslatesData(
    val wordid: Int,
    val word: String,
    val translate1: String,
    val translate2: String,
    var status: Int,
    var repeatdate: Long,
)