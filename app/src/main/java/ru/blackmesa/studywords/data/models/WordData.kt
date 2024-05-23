package ru.blackmesa.studywords.data.models

data class WordData(
    val wordid: Int,
    val word: String,
    val translate: String,
    val baseprogress: Int,
    var newprogress: Int,
    var answerdate: Long,
)