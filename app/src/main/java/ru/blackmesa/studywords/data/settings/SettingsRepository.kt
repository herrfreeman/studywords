package ru.blackmesa.studywords.data.settings

import ru.blackmesa.studywords.data.models.Credentials

interface SettingsRepository {

    var userKey: String
    var userId: Int
    var nightMode: Boolean

    fun getCredentials(): Credentials
    fun setCredentials(credentials: Credentials)
}