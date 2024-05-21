package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.AppSettings

interface SettingsInteractor {
    fun getSettings(): AppSettings
    fun saveSettings(settings: AppSettings)

    fun setUserKey(userKey: String)
    fun setUserNamePassword(userName: String, password: String)
}