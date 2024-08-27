package ru.blackmesa.studywords.domain

import android.content.Context
import ru.blackmesa.studywords.data.AuthRepository

class AuthInteractorImpl(
    private val context: Context,
    private val reposytory: AuthRepository,
) : AuthInteractor {

    override suspend fun singIn(userName: String, password: String) = reposytory.signIn(userName, password)
    override suspend fun createUser(userName: String, password: String) = reposytory.createUser(userName, password)
    override suspend fun confirmCreate(userName: String, password: String, code: String) = reposytory.confirmCreate(userName, password, code)

}