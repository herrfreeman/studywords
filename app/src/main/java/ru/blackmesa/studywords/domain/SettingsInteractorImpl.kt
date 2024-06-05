package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.Credentials
import ru.blackmesa.studywords.data.settings.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {


    override var userKey = repository.userKey
    override var userId = repository.userId
    override var nightMode = repository.nightMode

    override fun getCredentials() = repository.getCredentials()
    override fun setCredentials(credentials: Credentials) = repository.setCredentials(credentials)

}