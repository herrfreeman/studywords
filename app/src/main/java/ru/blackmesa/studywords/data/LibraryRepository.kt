package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.UpdateResult

interface LibraryRepository {

    suspend fun updateAllData(): UpdateResult
}