package ru.blackmesa.studywords.data.models

data class AppSettings(
    var nightMode: Boolean = false,
    var userKey: String = "",
    var userId: Int = 0,
)
