package ru.blackmesa.studywords.domain

sealed class ConfirmResult {

    class NoConnection: ConfirmResult()
    class Success: ConfirmResult()
    class TryAnotherCode(): ConfirmResult()
    data class Error(val errorCode: String, val errorMessage: String): ConfirmResult()

}