package ru.blackmesa.studywords.data

import android.content.Context
import kotlinx.coroutines.delay
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.network.AuthRequest
import ru.blackmesa.studywords.data.network.AuthResponse
import ru.blackmesa.studywords.data.network.ConfirmRequest
import ru.blackmesa.studywords.data.network.CreateUserRequest
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.settings.SettingsRepository
import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.CreateUserResult
import ru.blackmesa.studywords.getSecretKey
import java.security.MessageDigest


class AuthRepositoryImpl(
    private val context: Context,
    private val settings: SettingsRepository,
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) : AuthRepository {

    override suspend fun signIn(userName: String, password: String): AuthResult {

        if (userName.isEmpty()) {
            return AuthResult.Error("", "Login is empty")
        }
        if (password.isEmpty()) {
            return AuthResult.Error("", "Password is empty")
        }

        val response = networkClient.doRequest(AuthRequest(userName, password))
        return when (response.resultCode) {
            -1 -> AuthResult.NoConnection()
            200 -> {
                settings.userKey = (response as AuthResponse).userkey
                settings.userId = (response as AuthResponse).userid
                delay(1500)
                AuthResult.Success()
            }

            401 -> {
                settings.userKey = ""
                settings.userId = 0

                if (response.errorCode == "B0105") {
                    AuthResult.WrongPassword()
                } else {
                    AuthResult.Error(response.errorCode, response.errorMessage)
                }
            }
            else -> AuthResult.Error(response.resultCode.toString(), "[${response.errorCode}] ${response.errorMessage}")
        }
    }

    override suspend fun createUser(userName: String): CreateUserResult {

        val hashKey =  MessageDigest.getInstance("SHA-256")
            .digest("$userName|${getSecretKey()}".toByteArray())
            .joinToString("") { "%02x".format(it) }

        val response = networkClient.doRequest(CreateUserRequest(userName, "create", hashKey))
        return when (response.resultCode) {
            -1 -> CreateUserResult.NoConnection()
            200 -> {
                delay(1500)
                CreateUserResult.Success()
            }
            else -> CreateUserResult.Error(response.resultCode.toString(), "[${response.errorCode}] ${response.errorMessage}")
        }
    }

    override suspend fun confirmCreate(userName: String, password: String, code: String): AuthResult {

        if (userName.isEmpty()) {
            return AuthResult.Error("", "Login is empty")
        }
        if (password.isEmpty()) {
            return AuthResult.Error("", "Password is empty")
        }

        val response = networkClient.doRequest(ConfirmRequest(userName, password, "create", code))
        return when (response.resultCode) {
            -1 -> AuthResult.NoConnection()
            200 -> {
                settings.userKey = (response as AuthResponse).userkey
                settings.userId = (response as AuthResponse).userid
                delay(1500)
                AuthResult.Success()
            }
            else -> AuthResult.Error(response.resultCode.toString(), "[${response.errorCode}] ${response.errorMessage}")
        }
    }

}