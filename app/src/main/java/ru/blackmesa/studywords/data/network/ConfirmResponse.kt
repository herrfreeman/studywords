package ru.blackmesa.studywords.data.network

data class ConfirmResponse (
    val userkey: String,
    val userid: Int,
    val message: String,
) : Response()