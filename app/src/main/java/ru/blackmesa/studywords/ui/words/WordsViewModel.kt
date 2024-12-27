package ru.blackmesa.studywords.ui.words

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.domain.LibraryInteractor

class WordsViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val dictId: Int,
    private val dictName: String,
) : AndroidViewModel(application) {

    private val contentLiveData = MutableLiveData<List<WordData>>()
    private val words = emptyList<WordData>().toMutableList()
    fun observeContent(): LiveData<List<WordData>> = contentLiveData

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        viewModelScope.launch {
            words.clear()
            words.addAll(libInteractor.getWords(dictId))
            contentLiveData.postValue(words)
        }
    }

    fun getDictName() = dictName

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

}