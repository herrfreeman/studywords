package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.Credentials

interface SettingsInteractor {

    var userName: String
    var userKey: String
    var userId: Int
    var nightMode: Boolean

    fun loadCredentials(): Credentials
    fun saveCredentials(credentials: Credentials)

}