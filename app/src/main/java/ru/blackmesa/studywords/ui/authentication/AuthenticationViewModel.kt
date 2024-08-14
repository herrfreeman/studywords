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

    private var credentials = Credentials()
    private var errorMessage = ""
    private var confirmErrorMessage = ""

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
                    is AuthResult.Success -> AuthState.Success()
                    is AuthResult.Error -> {
                        errorMessage = authResult.errorMessage
                        AuthState.Default(credentials, errorMessage)
                    }

                    is AuthResult.NoConnection -> {
                        errorMessage = "No internet connection"
                        AuthState.Default(credentials, errorMessage)
                    }

                    is AuthResult.WrongPassword -> {
                        errorMessage = "Wrong password"
                        AuthState.Default(credentials, errorMessage)
                    }
                }
            )
        }
    }

    fun createAccount(userName: String, password: String) {
        credentials = Credentials(userName, password)
        authLiveData.postValue(AuthState.DefaultLoading(credentials, errorMessage))

        settingsInteractor.userId = 0
        settingsInteractor.userKey = ""

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val createResult = authInteractor.createUser(
                userName = credentials.userName,
            )

            authLiveData.postValue(
                when (createResult) {
                    is CreateUserResult.Success -> {
                        confirmErrorMessage = ""
                        AuthState.CreateConfirmation(
                            credentials = credentials,
                            errorMessage = errorMessage,
                            confirmCode = "",
                            confirmErrorMessage = confirmErrorMessage
                        )
                    }
                    is CreateUserResult.Error -> {
                        errorMessage = createResult.errorMessage
                        AuthState.Default(credentials, errorMessage)
                    }
                    is CreateUserResult.NoConnection -> {
                        errorMessage = "No internet connection"
                        AuthState.Default(credentials, errorMessage)
                    }
                }
            )

        }
    }

//    fun confirmCreate(userName: String, password: String, code: String) {
//        credentials = Credentials(userName, credentials.password)
//        authLiveData.postValue(
//            AuthState.ConfirmationLoading(credentials, AuthState.ConfirmMode.Create, "")
//        )
//
//        settingsInteractor.userId = 0
//        settingsInteractor.userKey = ""
//
//        viewModelScope.launch {
//            settingsInteractor.saveCredentials(credentials)
//
//            val confirmResult = authInteractor.confirmCreate(
//                userName = credentials.userName,
//                password = credentials.password,
//                code = code,
//            )
//
//            authLiveData.postValue(
//                when (confirmResult) {
//                    is AuthResult.Success -> AuthState.Success(credentials)
//                    is AuthResult.Error -> AuthState.OtherError(
//                        credentials,
//                        confirmResult.errorMessage
//                    )
//
//                    is AuthResult.NoConnection -> AuthState.NoInternet(credentials)
//                    is AuthResult.WrongPassword -> AuthState.PasswordError(credentials)
//                }
//            )
//        }
//    }

}