package ru.blackmesa.studywords.data.models

sealed class AuthState(val credentials: Credentials, val errorMessage: String = "") {

    sealed interface ConfirmMode {
        object Create : ConfirmMode
        object Restore : ConfirmMode
    }

    class Default(credentials: Credentials, errorMessage: String = "") :
        AuthState(credentials, errorMessage)

    class DefaultLoading(credentials: Credentials, errorMessage: String = "") :
        AuthState(credentials, errorMessage)

    class CreateConfirmation(
        credentials: Credentials,
        errorMessage: String,
        val confirmCode: String,
        val confirmErrorMessage: String = ""
    ) : AuthState(credentials, errorMessage)

    class CreateConfirmationLoading(
        credentials: Credentials,
        errorMessage: String,
        val confirmCode: String,
        val confirmErrorMessage: String = ""
    ) : AuthState(credentials, errorMessage)

    class Success(credentials: Credentials) : AuthState(credentials)


}