package ru.blackmesa.studywords.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.network.AuthRequest
import ru.blackmesa.studywords.data.network.NetworkClient

class AuthenticationViewModel(
    application: Application,
    private val networkClient: NetworkClient,
) : AndroidViewModel(application) {

    val message = MutableLiveData<String>()

    fun auth(request: AuthRequest) {
        Log.d("STUDY_WORDS", "VM START")
        viewModelScope.launch {
            val response = networkClient.doRequest(request)
            message.postValue(response)
            Log.d("STUDY_WORDS", "ANSWER: $response")
        }
    }

}