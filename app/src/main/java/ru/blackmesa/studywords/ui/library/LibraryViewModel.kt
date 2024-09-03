package ru.blackmesa.studywords.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import ru.blackmesa.studywords.data.models.DataUpdateResult
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.domain.LibraryInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor

class LibraryViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {

    private val libraryState = MutableLiveData<LibraryState>()
    fun observeLibraryState(): LiveData<LibraryState> = libraryState

    private var updateJob: Job? = null
    private var libraryData: List<DictData> = emptyList()

    companion object {
        val UPDATE_DELAY = 300L
    }

    init {
        libraryState.postValue(LibraryState.Loading)
        loadLocalLibrary()
        updateLibrary()
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdate()
    }

    fun updateLibrary() {
        libraryState.postValue(LibraryState.Loading)
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(UPDATE_DELAY)
            processUpdateResult(libInteractor.updateAllData())
        }
    }

    fun downloadDictionary(dictionary: DictData) {
        libraryState.postValue(LibraryState.Loading)
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            delay(UPDATE_DELAY)
            processUpdateResult(libInteractor.updateDictionary(dictionary.id))
        }
    }

    private suspend fun processUpdateResult(updateResult: DataUpdateResult) {
        when (updateResult) {
            is DataUpdateResult.Error ->
                libraryState.postValue(LibraryState.LibraryCurrent(libraryData))

            is DataUpdateResult.DataUpdated -> {
                libraryData = libInteractor.getDictionariesWithProgress()
                libraryState.postValue(LibraryState.LibraryUpdated(libraryData))
            }

            is DataUpdateResult.NoConnection -> {
                libraryData = libInteractor.getDictionariesWithProgress()
                libraryState.postValue(LibraryState.NoConnection(libraryData))
            }

            is DataUpdateResult.NotSignedIn -> libraryState.postValue(LibraryState.NotAuthorized)
            is DataUpdateResult.Synchronized -> {
                libraryData = libInteractor.getDictionariesWithProgress()
                libraryState.postValue(LibraryState.LibraryCurrent(libraryData))
            }
        }

    }

    fun stopUpdate() {
        updateJob?.cancel()
    }

    fun setLoadingState() {
        libraryState.postValue(LibraryState.Loading)
    }

    fun signOut() {
        settingsInteractor.userId = 0
        settingsInteractor.userKey = ""
        libraryState.postValue(LibraryState.NotAuthorized)
    }

    fun loadLocalLibrary() {
        libraryState.postValue(LibraryState.Loading)
        viewModelScope.launch {
            libraryData = libInteractor.getDictionariesWithProgress()
            libraryState.postValue(LibraryState.LibraryCurrent(libraryData))
        }
    }

    fun wipeAllLocalData() {
        libraryState.postValue(LibraryState.Loading)
        viewModelScope.launch {
            libraryState.postValue(LibraryState.Loading)
            libInteractor.wipeAllLocalData()
            updateLibrary()
        }
    }

}