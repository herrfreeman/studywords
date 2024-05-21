package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.settings.SettingsRepository
import ru.blackmesa.studywords.data.models.AppSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getSettings(): AppSettings = repository.getSettings()
    override fun saveSettings(settings: AppSettings) = repository.setSettings(settings)
    override fun setUserKey(userKey: String) {
        saveSettings(getSettings().apply { this.userKey = userKey })
    }
    override fun setUserNamePassword(userName: String, password: String) {
        saveSettings(getSettings().apply {
            this.userName = userName
            this.password = password
        })
    }
}