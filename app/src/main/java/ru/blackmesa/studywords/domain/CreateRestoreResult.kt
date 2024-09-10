package ru.blackmesa.studywords.domain

sealed class CreateRestoreResult {

    class NoConnection: CreateRestoreResult()
    class Success: CreateRestoreResult()
    data class Error(val errorCode: String, val errorMessage: String): CreateRestoreResult()

}