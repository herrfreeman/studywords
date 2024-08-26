package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.Credentials
import ru.blackmesa.studywords.data.settings.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {

    override var userName = repository.userName
    override var userKey = repository.userKey
    override var userId = repository.userId
    override var nightMode = repository.nightMode

    override fun loadCredentials() = repository.loadCredentials()
    override fun saveCredentials(credentials: Credentials) = repository.saveCredentials(credentials)

}