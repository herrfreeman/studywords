package ru.blackmesa.studywords.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import ru.blackmesa.studywords.data.models.AppSettings
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Credentials
import ru.blackmesa.studywords.domain.LibraryInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor

class AuthenticationViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val libraryInteractor: LibraryInteractor,

    ) : AndroidViewModel(application) {

    private val credentioalsLiveData = MutableLiveData<Credentials>()
    fun observeCredentials(): LiveData<Credentials> = credentioalsLiveData

    private val authLiveData = MutableLiveData<AuthState>()
    fun observeAuthState(): LiveData<AuthState> = authLiveData

    init {
        viewModelScope.launch {
            credentioalsLiveData.postValue(settingsInteractor.getCredentials())
        }
    }

    fun singIn(userName: String, password: String) {
        authLiveData.postValue(AuthState.Connecting)

        settingsInteractor.userId = 0
        settingsInteractor.userKey = ""

        viewModelScope.launch {
            settingsInteractor.setCredentials(Credentials(userName, password))
            authLiveData.postValue(
                libraryInteractor.singIn(userName, password)
            )
        }
    }

}