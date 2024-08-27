package ru.blackmesa.studywords.domain

interface AuthInteractor {

    suspend fun singIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String, password: String): CreateUserResult
    suspend fun confirmCreate(userName: String, password: String, code: String): ConfirmResult

}