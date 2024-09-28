package ru.blackmesa.studywords.data.models

sealed interface DataUpdateResult {

    object Synchronized : DataUpdateResult

    object DataUpdated: DataUpdateResult

    object NoConnection : DataUpdateResult

    object NotSignedIn : DataUpdateResult

    data class Error(
        val message: String
    ) : DataUpdateResult

}