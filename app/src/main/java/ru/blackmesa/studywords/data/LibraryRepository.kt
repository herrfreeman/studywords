package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordData

interface LibraryRepository {

    suspend fun updateAllData(): UpdateResult
    suspend fun signIn(userName: String, password: String): AuthState
    suspend fun getDictionaries(): List<Dictionary>
    suspend fun getWords(dictId: Int): List<WordData>
    suspend fun setProgress(progress: List<Progress>)

}