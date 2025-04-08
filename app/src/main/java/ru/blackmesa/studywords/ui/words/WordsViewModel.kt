package ru.blackmesa.studywords.ui.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.domain.LibraryInteractor

class WordsViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val dictId: Int,
    private val dictName: String,
    private val dictIsTotal: Boolean,
) : AndroidViewModel(application) {

    private val words = emptyList<WordData>().toMutableList()
    private var latestSearchText: String? = null
    var searchJob: Job? = null
    private val contentLiveData = MutableLiveData<List<WordData>>()
    fun observeContent(): LiveData<List<WordData>> = contentLiveData

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }

    init {
        viewModelScope.launch {
            words.clear()
            words.addAll(
                if (dictIsTotal) libInteractor.getAllWords()
                else libInteractor.getWords(dictId)
            )
            contentLiveData.postValue(words)
        }
    }

    fun getDictName() = dictName

    fun getDictQuantity() = words.count()

    override fun onCleared() {
        super.onCleared()
    }

    fun clearProgress(words: List<WordData>) {
        words.onEach { it.status = 0 }
        viewModelScope.launch {
            libInteractor.setProgress(words.map {
                Progress(
                    wordId = it.wordid,
                    status = it.status,
                    repeatDate = 0L,
                    version = System.currentTimeMillis() / 1000,
                    touched = true,
                )
            })
        }
        contentLiveData.postValue(words)
    }

    fun getWordsToStudy(): List<WordData> {
        val currentTimestamp = System.currentTimeMillis() / 1000
        return words
            .filter { it.status < 12 && (it.repeatdate == 0L || it.repeatdate <= currentTimestamp) }
            .shuffled()
            .sortedBy { if (it.repeatdate > 0) 1 else 2 }
            .take(10)
    }

    fun getNextRepeatTimestamp(): Long? {
        val notDoneList = words.filter { it.status < 12 }
        return if (notDoneList.isEmpty()) {
            null
        } else {
            notDoneList.minBy { it.repeatdate }.repeatdate
        }
    }

    fun getDoneWords(): List<WordData> = words
        .filter { it.status == 12 }
        .shuffled()

    fun search(
        changedText: String,
    ) {
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (changedText.isNullOrEmpty()) {
                contentLiveData.postValue(words)
            } else {
                postFilteredWords(changedText)
            }
        }
    }

    private fun postFilteredWords(filterString: String) {
        contentLiveData.postValue(words.filter {
            it.word.contains(filterString) || it.translate.contains(filterString)
        })
    }

    fun searchImmediately(queryText: String) {
        searchJob?.cancel()
        postFilteredWords(queryText)
    }

    fun getDictIsTotal() = dictIsTotal
}