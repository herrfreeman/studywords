package ru.blackmesa.studywords.ui.library

import ru.blackmesa.studywords.data.models.DictData

sealed interface LibraryState {

    object Loading : LibraryState

    object NotAuthorized : LibraryState

    data class NoConnection(val library: List<DictData>) : LibraryState

    data class LibraryCurrent(val library: List<DictData>) : LibraryState

    data class LibraryUpdated(val library: List<DictData>) : LibraryState

    data class UpdateError(val library: List<DictData>, val error: String) : LibraryState
}