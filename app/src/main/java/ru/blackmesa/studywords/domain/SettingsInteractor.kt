package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.AppSettings

interface SettingsInteractor {
    fun getSettings(): AppSettings
    fun saveSettings(settings: AppSettings)
}