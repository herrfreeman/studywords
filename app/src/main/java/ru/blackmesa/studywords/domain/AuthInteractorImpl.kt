package ru.blackmesa.studywords.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.blackmesa.studywords.data.AuthRepository
import ru.blackmesa.studywords.data.LibraryRepository
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordInDict

class AuthInteractorImpl(
    private val context: Context,
    private val reposytory: AuthRepository,
) : AuthInteractor {

    override suspend fun singIn(userName: String, password: String) = reposytory.signIn(userName, password)
    override suspend fun createUser(userName: String) = reposytory.createUser(userName)
    override suspend fun confirmCreate(userName: String, password: String, code: String) = reposytory.confirmCreate(userName, password, code)

}