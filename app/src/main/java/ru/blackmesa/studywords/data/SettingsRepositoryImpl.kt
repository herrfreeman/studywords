package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.AppSettings

class SettingsRepositoryImpl(private val localStorage: LocalSettingsStorage) : SettingsRepository {
    override fun getSettings(): AppSettings = localStorage.getSettings()
    override fun setSettings(settings: AppSettings) = localStorage.setSettings(settings)
}