package ru.blackmesa.studywords.ui.study

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.domain.LibraryInteractor

class StudyViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val wordList: List<WordData>,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<StudyState>()
    fun observeState(): LiveData<StudyState> = stateLiveData

    private lateinit var currentWord: WordData

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        nextQuestion()
    }

    fun showAnswer() {
        stateLiveData.postValue(StudyState.Answer(currentWord))
    }

    fun gotResult(isCorrect: Boolean) {


        currentWord.answerdate = System.currentTimeMillis() / 1000
        if (isCorrect) {
            currentWord.newprogress++
            if (currentWord.newprogress > 4) currentWord.newprogress = 4
        } else {
            currentWord.newprogress--
            currentWord.newprogress--
            if (currentWord.newprogress < 0) currentWord.newprogress = 0
        }

        viewModelScope.launch {
            libInteractor.setProgress(
                listOf(
                    Progress(
                        wordid = currentWord.wordid,
                        status = currentWord.newprogress,
                        answerdate = currentWord.answerdate,
                        version = 0,
                    )
                )
            )
        }

        nextQuestion()

    }


    fun nextQuestion() {

        wordList
            .filter { it.newprogress < it.baseprogress + 2 && it.newprogress < 4 }
            .apply {
                if (isEmpty()) {
                    stateLiveData.postValue(StudyState.Finish)
                } else {
                    currentWord = random()
                    stateLiveData.postValue(StudyState.Question(currentWord, size))
                }
            }

    }

}