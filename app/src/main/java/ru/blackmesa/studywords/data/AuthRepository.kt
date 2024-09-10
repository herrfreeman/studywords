package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.ConfirmResult
import ru.blackmesa.studywords.domain.CreateRestoreResult

interface AuthRepository {
    suspend fun signIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String): CreateRestoreResult
    suspend fun restorePassword(userName: String): CreateRestoreResult
    suspend fun confirmCreate(userName: String, password: String, code: String): ConfirmResult
    suspend fun confirmRestore(userName: String, password: String, code: String): ConfirmResult
}