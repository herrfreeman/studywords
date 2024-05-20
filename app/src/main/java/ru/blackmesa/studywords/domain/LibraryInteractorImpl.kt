package ru.blackmesa.studywords.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.blackmesa.studywords.data.LibraryRepository
import ru.blackmesa.studywords.data.models.UpdateResult

class LibraryInteractorImpl(
    private val context: Context,
    private val reposytory: LibraryRepository,
) : LibraryInteractor {

    override suspend fun updateAllData() = reposytory.updateAllData()

}