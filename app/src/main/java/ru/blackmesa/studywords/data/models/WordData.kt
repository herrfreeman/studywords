package ru.blackmesa.studywords.data.models

import java.io.Serializable

data class WordData(
    val wordid: Int,
    val word: String,
    var status: Int,
    var repeatdate: Long,
    val translate: String,
) : Serializable