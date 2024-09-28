package ru.blackmesa.studywords.domain

import android.content.Context
import ru.blackmesa.studywords.data.AuthRepository
import java.security.MessageDigest

class AuthInteractorImpl(
    private val context: Context,
    private val reposytory: AuthRepository,
) : AuthInteractor {

    override suspend fun singIn(userName: String, password: String) =
        reposytory.signIn(userName, generateHash(password))

    override suspend fun createUser(userName: String) =
        reposytory.createUser(userName)

    override suspend fun restorePassword(userName: String) =
        reposytory.restorePassword(userName)

    override suspend fun confirmCreate(userName: String, password: String, code: String) =
        reposytory.confirmCreate(userName, generateHash(password), code)

    override suspend fun confirmRestore(userName: String, password: String, code: String) =
        reposytory.confirmRestore(userName, generateHash(password), code)

    private fun generateHash(string: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(string.toByteArray())
            .joinToString("")
    }

}