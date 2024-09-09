package ru.blackmesa.studywords.ui.library

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.DataUpdateResult
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.domain.AnaliticsInteractor
import ru.blackmesa.studywords.domain.LibraryInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor

class LibraryViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val analitics: AnaliticsInteractor,
) : AndroidViewModel(application) {

    private val libraryState = MutableLiveData<LibraryState>()
    fun observeLibraryState(): LiveData<LibraryState> = libraryState

    private var updateJob: Job? = null
    private var libraryData: List<DictData> = emptyList()

    companion object {
        val UPDATE_DELAY = 300L
    }

    init {
        Log.d("STUDY_WORDS", "Init view model")
//        libraryState.postValue(LibraryState.Loading)
//        loadLocalLibrary()
//        updateLibrary()
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
            analitics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, "Load dict: ${dictionary.name}")
            processUpdateResult(libInteractor.updateDictionary(dictionary.id))
        }
    }

    private suspend fun processUpdateResult(updateResult: DataUpdateResult) {
        when (updateResult) {
            is DataUpdateResult.Error -> {
                analitics.logError("Update error: $updateResult.message")
                libraryState.postValue(LibraryState.UpdateError(libraryData, updateResult.message))
                Log.d("STUDY_WORDS", "Update error: ${updateResult.message}")
            }

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