package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordInDict

interface LibraryRepository {

    suspend fun updateAllData(): UpdateResult
    suspend fun signIn(): AuthState
    suspend fun getDictionaries(): List<Dictionary>
    suspend fun getWords(dictId: Int): List<WordInDict>


}