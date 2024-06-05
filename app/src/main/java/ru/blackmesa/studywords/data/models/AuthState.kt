package ru.blackmesa.studywords.data.models

sealed interface AuthState {

    object NoConnection : AuthState

    object Connecting : AuthState

    data class Error(
        val errorMessage: String
    ) : AuthState

    data class Success(
        val message: String
    ) : AuthState

}