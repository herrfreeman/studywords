package ru.blackmesa.studywords.data.settings

import ru.blackmesa.studywords.data.models.Credentials

class SettingsRepositoryImpl(private val localStorage: LocalSettingsStorage) : SettingsRepository {


    private val settings = localStorage.getSettings()

    override var userKey: String
        get() = settings.userKey
        set(value) {
            settings.userKey = value
            localStorage.setSettings(settings)
        }

    override var userId: Int
        get() = settings.userId
        set(value) {
            settings.userId = value
            localStorage.setSettings(settings)
        }

    override var nightMode: Boolean
        get() = settings.nightMode
        set(value) {
            settings.nightMode = value
            localStorage.setSettings(settings)
        }

    override fun loadCredentials(): Credentials = localStorage.getCredentials()
    override fun saveCredentials(credentials: Credentials) = localStorage.setCredentials(credentials)

}