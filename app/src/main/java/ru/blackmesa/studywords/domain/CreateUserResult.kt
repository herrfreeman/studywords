package ru.blackmesa.studywords.domain

sealed class CreateUserResult {

    class NoConnection: CreateUserResult()
    class Success: CreateUserResult()
    data class Error(val errorCode: String, val errorMessage: String): CreateUserResult()

}