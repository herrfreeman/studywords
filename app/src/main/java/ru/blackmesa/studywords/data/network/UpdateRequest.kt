package ru.blackmesa.studywords.data.network

data class UpdateRequest(
    val userkey: String,
    val dictversion: Long,
    val wordsversion: Long,
    val wordsindictsversion: Long,
    val wordtranslateversion: Long,
)