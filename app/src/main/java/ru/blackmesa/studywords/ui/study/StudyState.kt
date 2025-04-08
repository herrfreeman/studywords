package ru.blackmesa.studywords.ui.study

import ru.blackmesa.studywords.data.models.WordData

sealed interface StudyState {

companion object {
    val STAGE_QUESTION = 0
    val STAGE_ANSWER = 1
}

    data class Study(val word: WordData, val stage: Int, val wordsLeft: Int) : StudyState

    data class Loading(val word: WordData, val stage: Int, val wordsLeft: Int) : StudyState

    data class ComplainSuccess(val word: WordData, val stage: Int, val wordsLeft: Int) : StudyState

    data class ComplainError(val word: WordData, val stage: Int, val wordsLeft: Int) : StudyState

    object Finish : StudyState

}