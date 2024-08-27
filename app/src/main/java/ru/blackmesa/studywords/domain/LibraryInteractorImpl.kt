package ru.blackmesa.studywords.domain

import android.content.Context
import ru.blackmesa.studywords.data.LibraryRepository
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Progress

class LibraryInteractorImpl(
    private val context: Context,
    private val reposytory: LibraryRepository,
) : LibraryInteractor {

    override suspend fun updateAllData() = reposytory.updateLibraryFromServer()
    override suspend fun getDictionaries() = reposytory.getDictionaries()
    override suspend fun getWords(dictId: Int) = reposytory.getWords(dictId)
    override suspend fun setProgress(progress: List<Progress>) = reposytory.setProgress(progress)
    override suspend fun getDictionariesWithProgress(): List<DictData> = reposytory.getDictionariesWithProgress()
    override suspend fun wipeAllLocalData() = reposytory.wipeAllLocalData()

}