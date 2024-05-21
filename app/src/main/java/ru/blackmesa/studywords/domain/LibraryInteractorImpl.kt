package ru.blackmesa.studywords.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.blackmesa.studywords.data.LibraryRepository
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordInDict

class LibraryInteractorImpl(
    private val context: Context,
    private val reposytory: LibraryRepository,
) : LibraryInteractor {

    override suspend fun updateAllData() = reposytory.updateAllData()
    override suspend fun singIn() = reposytory.signIn()
    override suspend fun getDictionaries() = reposytory.getDictionaries()
    override suspend fun getWords(dictId: Int) = reposytory.getWords(dictId)

}