package ru.blackmesa.studywords.data.models

data class DraftWordData(
    val wordid: Int,
    val word: String,
    var status: Int,
    var repeatdate: Long,
)