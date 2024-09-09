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
import ru.blackmesa.studywords.domain.ConfirmResult
import ru.blackmesa.studywords.domain.CreateUserResult
import ru.blackmesa.studywords.domain.SettingsInteractor

class AuthenticationViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val authInteractor: AuthInteractor,
    private val analitics: AnaliticsInteractor,
) : AndroidViewModel(application) {

    private var credentials = Credentials()
    private var errorMessage = ""
    private var confirmErrorMessage = ""
    private var confirmMode = 0

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
                password = credentials.password
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

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val createResult = authInteractor.createUser(
                userName = credentials.userName,
                password = credentials.password
            )

            authLiveData.postValue(
                when (createResult) {
                    is CreateUserResult.Success -> {
                        confirmErrorMessage = ""
                        confirmMode = CREATE_CONFIRMATION
                        AuthState.CreateConfirmation(
                            credentials = credentials,
                            errorMessage = errorMessage,
                            confirmCode = "",
                            confirmErrorMessage = confirmErrorMessage
                        )
                    }

                    is CreateUserResult.Error -> {
                        errorMessage = createResult.errorMessage
                        analitics.logError("Create error: $errorMessage")
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


    fun confirm(confirmCode: String) {
        when (confirmMode) {
            CREATE_CONFIRMATION -> createConfirmation(confirmCode)
            RESTORE_CONFIRMATION -> {}
            else -> {}
        }
    }

    private fun createConfirmation(confirmCode: String) {
        authLiveData.postValue(AuthState.CreateConfirmationLoading(
            credentials = credentials,
            errorMessage = errorMessage,
            confirmCode = confirmCode,
            confirmErrorMessage = confirmErrorMessage,
        ))

        viewModelScope.launch {

            val confirmResult = authInteractor.confirmCreate(
                userName = credentials.userName,
                password = credentials.password,
                code = confirmCode
            )

            authLiveData.postValue(
                when (confirmResult) {
                    is ConfirmResult.Success -> {
                        analitics.logEvent(FirebaseAnalytics.Event.SIGN_UP)
                        confirmErrorMessage = ""
                        AuthState.Success(credentials)
                    }

                    is ConfirmResult.TryAnotherCode -> {
                        confirmErrorMessage = "Try another code"
                        AuthState.CreateConfirmation(
                            credentials = credentials,
                            errorMessage = errorMessage,
                            confirmCode = confirmCode,
                            confirmErrorMessage = confirmErrorMessage,
                        )
                    }

                    is ConfirmResult.Error -> {
                        confirmErrorMessage = ""
                        errorMessage = confirmResult.errorMessage
                        analitics.logError("Confirm error: $errorMessage")
                        AuthState.Default(
                            credentials = credentials,
                            errorMessage = errorMessage,
                        )
                    }

                    is ConfirmResult.NoConnection -> {
                        errorMessage = "No internet connection"
                        AuthState.Default(credentials, errorMessage)
                    }

                }
            )
        }
    }

    companion object {
        const val CREATE_CONFIRMATION = 1
        const val RESTORE_CONFIRMATION = 2
    }

}