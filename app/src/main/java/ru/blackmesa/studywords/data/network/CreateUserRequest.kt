package ru.blackmesa.studywords.data.network

data class CreateUserRequest(val username: String, val mode: String, val key: String)