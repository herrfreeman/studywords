package ru.blackmesa.studywords.domain

sealed class AuthResult {

    class NoConnection: AuthResult()
    class Success: AuthResult()
    data class Error(val errorCode: String, val errorMessage: String): AuthResult()

}