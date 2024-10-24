package ru.blackmesa.studywords.data.models

sealed class AuthState(val credentials: Credentials, val errorMessage: String = "") {

    class Default(credentials: Credentials, errorMessage: String = "") :
        AuthState(credentials, errorMessage)

    class DefaultLoading(credentials: Credentials, errorMessage: String = "") :
        AuthState(credentials, errorMessage)

    class ConfirmationRequest(
        credentials: Credentials,
        errorMessage: String,
        val confirmCode: String,
        val confirmErrorMessage: String = ""
    ) : AuthState(credentials, errorMessage)

    class ConfirmationLoading(
        credentials: Credentials,
        errorMessage: String,
        val confirmCode: String,
        val confirmErrorMessage: String = ""
    ) : AuthState(credentials, errorMessage)

    class Success(credentials: Credentials) : AuthState(credentials)


}