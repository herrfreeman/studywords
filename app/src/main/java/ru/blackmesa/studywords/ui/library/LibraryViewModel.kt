package ru.blackmesa.studywords.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.domain.LibraryInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor

class LibraryViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {

    private val updateStateLiveData = MutableLiveData<UpdateResult>()
    fun observeUpdateState(): LiveData<UpdateResult> = updateStateLiveData

    private val dictionaryLiveData = MutableLiveData<List<DictData>>()
    fun observeDictionary(): LiveData<List<DictData>> = dictionaryLiveData

    private var updateJob: Job? = null

    companion object {
        val UPDATE_DELAY = 1000L
    }

    init {
        loadLibrary()
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdate()
    }

    fun startUpdate() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            //while (true) {
                updateStateLiveData.postValue(libInteractor.updateAllData())
                delay(UPDATE_DELAY)
            //}
        }
    }

    fun stopUpdate() {
        updateJob?.cancel()
    }

    fun signOut() {
        viewModelScope.launch {
            settingsInteractor.userId = 0
            settingsInteractor.userKey = ""
            updateStateLiveData.postValue(UpdateResult.NotSignedIn)
        }
    }

    fun loadLibrary() {
        viewModelScope.launch {
            dictionaryLiveData.postValue(libInteractor.getDictionariesWithProgress())
        }
    }

}