package ru.blackmesa.studywords.domain

interface AuthInteractor {
    suspend fun singIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String): CreateRestoreResult
    suspend fun restorePassword(userName: String): CreateRestoreResult
    suspend fun confirmCreate(userName: String, password: String, code: String): ConfirmResult
    suspend fun confirmRestore(userName: String, password: String, code: String): ConfirmResult
}