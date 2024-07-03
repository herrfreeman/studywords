package ru.blackmesa.studywords.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Credentials
import ru.blackmesa.studywords.domain.AuthInteractor
import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.CreateUserResult
import ru.blackmesa.studywords.domain.SettingsInteractor

class AuthenticationViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val authInteractor: AuthInteractor,
) : AndroidViewModel(application) {

    private var credentials = Credentials("", "")
    private var confirmError = ""

    private val authLiveData = MutableLiveData<AuthState>()
    fun observeAuthState(): LiveData<AuthState> = authLiveData

    init {
        viewModelScope.launch {
            credentials = settingsInteractor.loadCredentials()
            authLiveData.postValue(AuthState.NotConnected(credentials))
        }
    }

    fun singIn(userName: String, password: String) {
        credentials = Credentials(userName, password)
        authLiveData.postValue(AuthState.NotConnectedLoading(credentials))

        settingsInteractor.userId = 0
        settingsInteractor.userKey = ""

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val authResult = authInteractor.singIn(
                userName = credentials.userName,
                password = credentials.password
            )

            authLiveData.postValue(
                when (authResult) {
                    is AuthResult.Success -> AuthState.Success(credentials)
                    is AuthResult.Error -> AuthState.OtherError(
                        credentials,
                        authResult.errorMessage
                    )

                    is AuthResult.NoConnection -> AuthState.NoInternet(credentials)
                    is AuthResult.WrongPassword -> AuthState.PasswordError(credentials)
                }
            )

        }
    }

    fun createUser(userName: String) {
        credentials = Credentials(userName, credentials.password)
        authLiveData.postValue(
            AuthState.Loading(credentials)
        )

        settingsInteractor.userId = 0
        settingsInteractor.userKey = ""

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val createResult = authInteractor.createUser(
                userName = credentials.userName,
            )

            authLiveData.postValue(
                when (createResult) {
                    is CreateUserResult.Success -> AuthState.Confirmation(
                        credentials,
                        AuthState.ConfirmMode.Create,
                        ""
                    )
                    is CreateUserResult.Error -> AuthState.OtherError(
                        credentials,
                        createResult.errorMessage
                    )
                    is CreateUserResult.NoConnection -> AuthState.NoInternet(credentials)
                }
            )

        }
    }

}