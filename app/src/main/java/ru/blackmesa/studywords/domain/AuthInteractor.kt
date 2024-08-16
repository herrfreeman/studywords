package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordData

interface AuthInteractor {

    suspend fun singIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String, password: String): CreateUserResult
    suspend fun confirmCreate(userName: String, password: String, code: String): ConfirmResult

}