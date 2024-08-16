package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.ConfirmResult
import ru.blackmesa.studywords.domain.CreateUserResult

interface AuthRepository {
    suspend fun signIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String, password: String): CreateUserResult
    suspend fun confirmCreate(userName: String, password: String, code: String): ConfirmResult
}