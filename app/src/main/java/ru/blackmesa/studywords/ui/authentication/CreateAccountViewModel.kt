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
import ru.blackmesa.studywords.domain.CreateRestoreResult
import ru.blackmesa.studywords.domain.SettingsInteractor

class CreateAccountViewModel(
    application: Application,
    private val settingsInteractor: SettingsInteractor,
    private val authInteractor: AuthInteractor,
    private val analitics: AnaliticsInteractor,
) : AndroidViewModel(application) {

    private var credentials = Credentials()
    private var errorMessage = ""
    private var confirmErrorMessage = ""

    private val authLiveData = MutableLiveData<AuthState>()
    fun observeAuthState(): LiveData<AuthState> = authLiveData

    init {
        //TODO errorMessage = сделать прием входящего параметра - ошибки
        authLiveData.postValue(AuthState.Default(credentials, errorMessage))
    }

    fun createAccount(userName: String, password: String) {
        credentials = Credentials(userName, password)
        authLiveData.postValue(AuthState.DefaultLoading(credentials, errorMessage))

        viewModelScope.launch {
            settingsInteractor.saveCredentials(credentials)

            val createResult = authInteractor.createUser(
                userName = credentials.userName,
            )

            authLiveData.postValue(
                when (createResult) {
                    is CreateRestoreResult.Success -> {
                        confirmErrorMessage = ""
                        AuthState.ConfirmationRequest(
                            credentials = credentials,
                            errorMessage = errorMessage,
                            confirmCode = "",
                            confirmErrorMessage = confirmErrorMessage
                        )
                    }

                    is CreateRestoreResult.Error -> {
                        errorMessage = createResult.errorMessage
                        analitics.logError("Create error: $errorMessage")
                        AuthState.Default(credentials, errorMessage)
                    }

                    is CreateRestoreResult.NoConnection -> {
                        errorMessage = "No internet connection"
                        AuthState.Default(credentials, errorMessage)
                    }
                }
            )
        }
    }

    fun confirm(confirmCode: String) {
        authLiveData.postValue(AuthState.ConfirmationLoading(
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
                        AuthState.ConfirmationRequest(
                            credentials = credentials,
                            errorMessage = errorMessage,
                            confirmCode = confirmCode,
                            confirmErrorMessage = confirmErrorMessage,
                        )
                    }

                    is ConfirmResult.Error -> {
                        confirmErrorMessage = ""
                        errorMessage = confirmResult.errorMessage
                        analitics.logError("Create confirm error: $errorMessage")
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

}