package ru.blackmesa.studywords.ui.study

import ru.blackmesa.studywords.data.models.WordWithTranslate

sealed interface StudyState {

    data class Question(val word: WordWithTranslate) : StudyState

    data class Answer(val word: WordWithTranslate) : StudyState

    object Finish : StudyState

}