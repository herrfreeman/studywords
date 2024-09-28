package ru.blackmesa.studywords.data.network

data class ConfirmRequest(val username: String, val password: String, val mode: String, val code: String)