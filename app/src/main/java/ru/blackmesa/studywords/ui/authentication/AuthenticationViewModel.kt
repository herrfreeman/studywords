package ru.blackmesa.studywords.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Credentials
import ru.blackmesa.studywords.domain.AnaliticsInteractor
import ru.blackmesa.studywords.domain.AuthInteractor
import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.SettingsInteractor

class AuthenticationViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val authInteractor: AuthInteractor,
    private val analitics: AnaliticsInteractor,
) : AndroidViewModel(application) {

    private var credentials = Credentials()
    private var errorMessage = ""

    private val authLiveData = MutableLiveData<AuthState>()
    fun observeAuthState(): LiveData<AuthState> = authLiveData

    init {
        //TODO errorMessage = сделать прием входящего параметра - ошибки
        authLiveData.postValue(AuthState.Default(credentials, errorMessage))
        viewModelScope.launch {
            credentials = settingsInteractor.loadCredentials()
            authLiveData.postValue(AuthState.Default(credentials, errorMessage))
        }
    }

    fun login(userName: String, password: String) {
        credentials = Credentials(userName, password)
        authLiveData.postValue(AuthState.DefaultLoading(credentials, errorMessage))

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val authResult = authInteractor.singIn(
                userName = credentials.userName,
                password = credentials.password,
            )

            authLiveData.postValue(
                when (authResult) {
                    is AuthResult.Success -> {
                        analitics.logEvent(FirebaseAnalytics.Event.LOGIN)
                        AuthState.Success(credentials)
                    }

                    is AuthResult.Error -> {
                        errorMessage = authResult.errorMessage
                        analitics.logError("Login error: $errorMessage")
                        AuthState.Default(credentials, errorMessage)
                    }

                    is AuthResult.NoConnection -> {
                        errorMessage = "No internet connection"
                        AuthState.Default(credentials, errorMessage)
                    }

                }
            )
        }
    }

}