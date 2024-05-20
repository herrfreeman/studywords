package ru.blackmesa.studywords.data.models

sealed interface UpdateResult {

    object Synchronized : UpdateResult

    object GotNewData : UpdateResult

    object NoConnection : UpdateResult

    object NotSignedIn : UpdateResult

    data class Error(
        val message: String
    ) : UpdateResult

}