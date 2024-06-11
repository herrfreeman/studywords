package ru.blackmesa.studywords.ui.study

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
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
    private val currentQueue: MutableList<WordData> = mutableListOf()

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        wordList.onEach { it.repeatdate = 0 }
        nextQuestion()
        //currentQueue.addAll(wordList.onEach { it.repeatdate = 0 })

    }

    fun showAnswer() {
        stateLiveData.postValue(StudyState.Answer(currentWord))
    }

    fun gotResult(isCorrect: Boolean) {

        if (isCorrect) {
            currentWord.status++
            currentWord.repeatdate = when (currentWord.status) {
                4 -> System.currentTimeMillis() / 1000 + 60 * 60 * 24 //Add 24H
                8 -> System.currentTimeMillis() / 1000 + 60 * 60 * 24 * 8 //Add 1W
                else -> 0L
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

        if (currentQueue.isEmpty()) {
            currentQueue.addAll(wordList.filter { it.repeatdate == 0L && it.status < 12 })
            currentQueue.shuffle()
        }

        if (currentQueue.isEmpty()) {
            viewModelScope.launch {
                libInteractor.setProgress(wordList.map {
                    Progress(
                        wordid = it.wordid,
                        status = it.status,
                        repeatdate = it.repeatdate,
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
                    wordList.filter { it.repeatdate == 0L && it.status < 12 }.size
                )
            )
        }


//        wordList
//            .filter { it.repeatdate == 0L && it.status < 12 }
//            .apply {
//                if (isEmpty()) {
//
//                    viewModelScope.launch {
//                        libInteractor.setProgress(wordList.map {
//                            Progress(
//                                wordid = it.wordid,
//                                status = it.status,
//                                repeatdate = it.repeatdate,
//                                version = System.currentTimeMillis() / 1000,
//                                touched = true,
//                            )
//                        })
//                        stateLiveData.postValue(StudyState.Finish)
//                    }
//
//                } else {
//                    currentWord = random()
//                    stateLiveData.postValue(StudyState.Question(currentWord, size))
//                }
//            }

    }

}