package ru.blackmesa.studywords.data.models

sealed interface UpdateResult {

    object Synchronized : UpdateResult

    data class  LibraryUpdated(
        val library: List<DictData>
    ) : UpdateResult

    object NoConnection : UpdateResult

    object NotSignedIn : UpdateResult

    data class Error(
        val message: String
    ) : UpdateResult

}