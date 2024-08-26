package ru.blackmesa.studywords.data.settings

import ru.blackmesa.studywords.data.models.Credentials

interface SettingsRepository {

    var userName: String
    var userKey: String
    var userId: Int
    var nightMode: Boolean

    fun loadCredentials(): Credentials
    fun saveCredentials(credentials: Credentials)
}