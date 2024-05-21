package ru.blackmesa.studywords.data.network

data class AuthResponse (
    val userkey: String,
    val message: String,
) : Response()