package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.settings.SettingsRepository
import ru.blackmesa.studywords.data.models.AppSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getSettings(): AppSettings = repository.getSettings()
    override fun saveSettings(settings: AppSettings) = repository.setSettings(settings)
}