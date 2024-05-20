package ru.blackmesa.studywords.domain

import ru.blackmesa.studywords.data.models.UpdateResult

interface LibraryInteractor {

    suspend fun updateAllData(): UpdateResult

}