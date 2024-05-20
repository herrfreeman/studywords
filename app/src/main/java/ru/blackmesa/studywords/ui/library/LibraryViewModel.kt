package ru.blackmesa.studywords.ui.library

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.domain.LibraryInteractor

class LibraryViewModel(
    application: Application,
    private val libInteractor: LibraryInteractor,
) : AndroidViewModel(application) {

    private val updateStateLiveData = MutableLiveData<UpdateResult>()
    fun observeUpdateState(): LiveData<UpdateResult> = updateStateLiveData

    private var currentUpdateStatus: UpdateResult? = null
    private var updateJob: Job? = null

    companion object {
        val UPDATE_DELAY = 1000L
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdate()
    }

    fun startUpdate() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            //while (true) {
                val newStatus = libInteractor.updateAllData()
                if (newStatus != currentUpdateStatus) {
                    currentUpdateStatus = newStatus
                    updateStateLiveData.postValue(currentUpdateStatus ?: UpdateResult.Synchronized)
                }
                delay(UPDATE_DELAY)
            //}
        }


    }

    fun stopUpdate() {
        updateJob?.cancel()
    }


    fun loadLibrary() {

    }

}