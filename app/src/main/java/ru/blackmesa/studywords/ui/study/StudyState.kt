package ru.blackmesa.studywords.ui.study

import ru.blackmesa.studywords.data.models.WordData

sealed interface StudyState {

    data class Question(
        val word: WordData,
        val wordsLeft: Int,
    ) : StudyState

    data class Answer(val word: WordData) : StudyState

    object Finish : StudyState

}