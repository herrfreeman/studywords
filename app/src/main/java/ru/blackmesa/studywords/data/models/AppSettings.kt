package ru.blackmesa.studywords.data.models

data class AppSettings(
    var nightMode: Boolean = false,
    var userName: String = "",
    var password: String = "",
    var userKey: String = "",
)
