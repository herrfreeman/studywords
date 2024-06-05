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
    private val dictionaryId: Int,
) : AndroidViewModel(application) {

    private val contentLiveData = MutableLiveData<List<WordData>>()
    fun observeContent(): LiveData<List<WordData>> = contentLiveData

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        viewModelScope.launch {
            contentLiveData.postValue(libInteractor.getWords(dictionaryId))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun clearProgress(words: List<WordData>) {
        words.onEach { it.status = 0 }
        viewModelScope.launch {
            libInteractor.setProgress(words.map {
                Progress(
                    wordid = it.wordid,
                    status = it.status,
                    repeatdate = 0L,
                    version = System.currentTimeMillis() / 1000,
                    touched = true,
                )
            })
        }
        contentLiveData.postValue(words)
    }


}