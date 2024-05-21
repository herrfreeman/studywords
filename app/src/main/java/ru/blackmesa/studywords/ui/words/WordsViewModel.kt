package ru.blackmesa.studywords.ui.words

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.domain.LibraryInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor

class WordsViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val dictionaryId: Int,
) : AndroidViewModel(application) {

    private val contentLiveData = MutableLiveData<List<Word>>()
    fun observeContent(): LiveData<List<Word>> = contentLiveData

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        Log.d("STUDY_WORDS_DEBUG", "Open dict id = $dictionaryId")
        viewModelScope.launch {
            contentLiveData.postValue(libInteractor.getWords(dictionaryId).map {
                Log.d("STUDY_WORDS_DEBUG", "Loaded = $it")
                Word(
                    id = it.wordid,
                    word = it.wordid.toString(),
                    version = it.version,
                )
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
    }


}