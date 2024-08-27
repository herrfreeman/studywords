package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.DataUpdateResult
import ru.blackmesa.studywords.data.models.WordData

interface LibraryRepository {

    suspend fun updateAllData(): DataUpdateResult
    suspend fun getDictionaries(): List<Dictionary>
    suspend fun getWords(dictId: Int): List<WordData>
    suspend fun setProgress(progress: List<Progress>)
    suspend fun getDictionariesWithProgress(): List<DictData>
    suspend fun wipeAllLocalData()
}