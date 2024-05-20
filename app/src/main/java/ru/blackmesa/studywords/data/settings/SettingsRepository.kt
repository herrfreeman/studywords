package ru.blackmesa.studywords.data.settings

import ru.blackmesa.studywords.data.models.AppSettings

interface SettingsRepository {
    fun getSettings(): AppSettings
    fun setSettings(settings: AppSettings)
}