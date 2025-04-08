package ru.blackmesa.studywords.data.network

data class ComplainRequest(
    val userid: Int,
    val userkey: String,
    val wordid: Int,
    val word: String,
    val translate1: String,
    val translate2: String,
)