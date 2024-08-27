package ru.blackmesa.studywords.ui.library

import ru.blackmesa.studywords.data.models.DictData

sealed interface LibraryState {

    object Start : LibraryState

    object NotAuthorized : LibraryState

    data class NoConnection(val library: List<DictData>) : LibraryState

    data class LibraryCurrent(val library: List<DictData>) : LibraryState

    data class LibraryUpdated(val library: List<DictData>) : LibraryState

}