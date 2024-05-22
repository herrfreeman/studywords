package ru.blackmesa.studywords.ui.study

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.blackmesa.studywords.data.models.WordWithTranslate
import ru.blackmesa.studywords.domain.LibraryInteractor

class StudyViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val wordList: List<WordWithTranslate>,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<StudyState>()
    fun observeState(): LiveData<StudyState> = stateLiveData

    private var curIndex = -1

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        nextQuestion()
    }

    fun showAnswer() {
        stateLiveData.postValue(StudyState.Answer(wordList[curIndex]))
    }

    fun nextQuestion() {
        curIndex++
        if (wordList.isEmpty() || curIndex > wordList.size - 1) {
            stateLiveData.postValue(StudyState.Finish)
        } else {
            stateLiveData.postValue(StudyState.Question(wordList[curIndex]))
        }
    }


}