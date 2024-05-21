package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordInDict

interface LibraryInteractor {

    suspend fun updateAllData(): UpdateResult
    suspend fun singIn(): AuthState
    suspend fun getDictionaries(): List<Dictionary>
    suspend fun getWords(dictId: Int): List<WordInDict>

}