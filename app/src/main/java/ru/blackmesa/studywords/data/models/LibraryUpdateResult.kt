package ru.blackmesa.studywords.data.models

sealed interface LibraryUpdateResult {

    object Synchronized : LibraryUpdateResult

    object LibraryUpdated: LibraryUpdateResult

    object NoConnection : LibraryUpdateResult

    object NotSignedIn : LibraryUpdateResult

    data class Error(
        val message: String
    ) : LibraryUpdateResult

}