package ru.blackmesa.studywords.ui.study

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.DataUpdateResult
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.domain.AnaliticsInteractor
import ru.blackmesa.studywords.domain.LibraryInteractor

class StudyViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val wordList: List<WordData>,
    private val studyMode: Int,
    private val analitics: AnaliticsInteractor,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<StudyState>()
    fun observeState(): LiveData<StudyState> = stateLiveData
    private var sendComplainJob: Job? = null

    private lateinit var currentWord: WordData
    private val currentQueue: MutableList<WordData> = mutableListOf()
    private var currentStage = 0

    companion object {
        val COMPLAIN_DELAY = 500L
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
        currentStage = StudyState.STAGE_ANSWER
        stateLiveData.postValue(StudyState.Study(currentWord, currentStage, getWordsLeft()))
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
        }

        nextQuestion()

    }

    fun setFullyStudied() {
        currentWord.status = 12
        nextQuestion()
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
            currentStage = StudyState.STAGE_QUESTION
            stateLiveData.postValue(StudyState.Study(currentWord, currentStage, getWordsLeft()))
        }

    }

    private fun getWordsLeft(): Int {
        return if (studyMode == StudyMode.COMMON) {
            wordList.filter { it.repeatdate == 0L && it.status < 12 }.size
        } else {
            currentQueue.size + 1
        }
    }

    fun wordInStraitStage(word: WordData): Boolean = if (studyMode == StudyMode.COMMON) {
        word.status in STRIGHT_STAGE
    } else {
        true
    }

    fun complain() {
        stateLiveData.postValue(StudyState.Loading(currentWord, currentStage, getWordsLeft()))


        sendComplainJob?.cancel()
        sendComplainJob = viewModelScope.launch {
            delay(COMPLAIN_DELAY)
            analitics.logEvent(FirebaseAnalytics.Event.POST_SCORE, "Complain: ${currentWord.word}")
            val complainResult = libInteractor.wordComplain(currentWord)
            when (complainResult) {
                DataUpdateResult.DataUpdated -> stateLiveData.postValue(
                    StudyState.ComplainSuccess(currentWord, currentStage, getWordsLeft())
                )
                is DataUpdateResult.Error, is DataUpdateResult.NoConnection -> stateLiveData.postValue(
                    StudyState.ComplainError(currentWord, currentStage, getWordsLeft())
                )
                else -> TODO()

            }
        }
    }

}