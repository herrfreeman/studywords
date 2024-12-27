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
    private val studyMode: Int,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<StudyState>()
    fun observeState(): LiveData<StudyState> = stateLiveData

    private lateinit var currentWord: WordData
    private val currentQueue: MutableList<WordData> = mutableListOf()

    companion object {
        val UPDATE_DELAY = 1000L
        val STRIGHT_STAGE = listOf(0, 1, 4, 5, 8, 9)
    }

    init {
        wordList.onEach { it.repeatdate = 0 }
        if (studyMode == StudyMode.CHECK_ONCE) {
            currentQueue.addAll(wordList)
        }
        nextQuestion()
    }

    fun showAnswer() {
        stateLiveData.postValue(StudyState.Answer(currentWord))
    }

    fun gotResult(isCorrect: Boolean) {

        if (isCorrect) {
            if (studyMode == StudyMode.COMMON) {
                currentWord.status++
                currentWord.repeatdate = when (currentWord.status) {
                    4 -> System.currentTimeMillis() / 1000 + 60 * 60 * 24 //Add 24H
                    8 -> System.currentTimeMillis() / 1000 + 60 * 60 * 24 * 7 //Add 1W
                    else -> 0L
                }
            }
        } else {
            currentWord.repeatdate = 0L
            currentWord.status = 0
            //currentWord.status = decreaseStatus(currentWord.status)
            //currentWord.status = decreaseStatus(currentWord.status)
        }

        nextQuestion()

    }

    fun setFullyStudied() {
        currentWord.status = 12
        nextQuestion()
    }

    private fun decreaseStatus(status: Int): Int {
        return when {
            status in intArrayOf(0, 4, 8) -> status
            else -> status - 1
        }
    }

    fun nextQuestion() {
        if (studyMode == StudyMode.COMMON) { //Renew study queue
            if (currentQueue.isEmpty()) {
                currentQueue.addAll(wordList.filter { it.repeatdate == 0L && it.status < 12 })
                currentQueue.shuffle()
            }
        }

        if (currentQueue.isEmpty()) {
            viewModelScope.launch {
                libInteractor.setProgress(wordList.map {
                    Progress(
                        wordId = it.wordid,
                        status = it.status,
                        repeatDate = it.repeatdate,
                        version = System.currentTimeMillis() / 1000,
                        touched = true,
                    )
                })
                stateLiveData.postValue(StudyState.Finish)
            }
        } else {
            currentWord = currentQueue.removeLast()
            stateLiveData.postValue(
                StudyState.Question(
                    currentWord,
                    if (studyMode == StudyMode.COMMON) {
                        wordList.filter { it.repeatdate == 0L && it.status < 12 }.size
                    } else {
                        currentQueue.size + 1
                    }
                )
            )
        }

    }

    fun wordInStraitStage(word: WordData): Boolean = if (studyMode == StudyMode.COMMON) {
        word.status in STRIGHT_STAGE
    } else {
        true
    }

}