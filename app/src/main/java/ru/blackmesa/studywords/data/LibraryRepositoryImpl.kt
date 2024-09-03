package ru.blackmesa.studywords.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.db.WordInDictEntity
import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.models.DataUpdateResult
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.data.network.DictionaryRequest
import ru.blackmesa.studywords.data.network.DictionaryResponse
import ru.blackmesa.studywords.data.network.LibraryRequest
import ru.blackmesa.studywords.data.network.LibraryResponse
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.network.ProgressRequest
import ru.blackmesa.studywords.data.network.ProgressResponse
import ru.blackmesa.studywords.data.settings.SettingsRepository

class LibraryRepositoryImpl(
    private val context: Context,
    private val settings: SettingsRepository,
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) : LibraryRepository {

    override suspend fun updateAllData(): DataUpdateResult {
        return withContext(Dispatchers.IO) {

            if (settings.userKey.isEmpty() || settings.userId == 0) {
                return@withContext DataUpdateResult.NotSignedIn
            }

            val lastLibrary = database.libraryDao().getDictVersion()
            val libVersion = if (lastLibrary.isEmpty()) 0L else lastLibrary.first()

            val request = LibraryRequest(
                userid = settings.userId,
                userkey = settings.userKey,
                version = libVersion,
            )

            val response = networkClient.doRequest(request)
            when (response.resultCode) {
                -1 -> DataUpdateResult.NoConnection
                200 -> {
                    // At first load dictionaries with words and translates inside of it
                    val updateResult = updateLibrary((response as LibraryResponse).dictionaries)
                    when (updateResult) {
                        is DataUpdateResult.DataUpdated,
                        is DataUpdateResult.Synchronized -> syncProgress()// if ok - sync progress
                        else -> updateResult
                    }
                }

                401 -> {
                    settings.userKey = ""
                    settings.userId = 0
                    DataUpdateResult.Error("Auth error from library")
                }

                else -> DataUpdateResult.Error("Update error: ${response.resultCode}")
            }
        }
    }


    override suspend fun getDictionaries(): List<Dictionary> {
        return withContext(Dispatchers.IO) {
            database.libraryDao().getDict().map { it.toDictionary() }
        }
    }

    override suspend fun getWords(dictId: Int): List<WordData> {
        return withContext(Dispatchers.IO) {
            database.libraryDao().getWords(dictId, settings.userId)
        }
    }

    override suspend fun setProgress(progress: List<Progress>) {
        withContext(Dispatchers.IO) {
            database.libraryDao()
                .insertProgress(progress.map { it.toEntity(settings.userId) })
        }
    }

    override suspend fun getDictionariesWithProgress(): List<DictData> {
        return withContext(Dispatchers.IO) {
            database.libraryDao().getDictData(settings.userId, System.currentTimeMillis() / 1000)
        }
    }

    override suspend fun wipeAllLocalData() {
        return withContext(Dispatchers.IO) {
            database.libraryDao().deleteDicts()
            database.libraryDao().deleteWords()
            database.libraryDao().deleteWordInDict()
            database.libraryDao().deleteTranslate()
            database.libraryDao().deleteProgress()
        }
    }

    private suspend fun updateLibrary(
        dictionaries: List<DictionaryDto>
    ): DataUpdateResult {
        //TODO сделать обновление словарей в транзакции
        return if (dictionaries.isEmpty()) {
            return DataUpdateResult.Synchronized
        } else {
            dictionaries.forEach {
                if (it.isDefault) {
                    val dictUpdateResult = updateDictionary(it.id)
                    when (dictUpdateResult) {
                        is DataUpdateResult.Error,
                        is DataUpdateResult.NoConnection,
                        is DataUpdateResult.NotSignedIn -> return dictUpdateResult

                        else -> Unit
                    }
                }
            }
            database.libraryDao().insertDict(dictionaries.map { it.toEntity() })
            DataUpdateResult.DataUpdated
        }
    }

    override suspend fun updateDictionary(dictId: Int): DataUpdateResult {
        return withContext(Dispatchers.IO) {

            val request = DictionaryRequest(
                userid = settings.userId,
                userkey = settings.userKey,
                dictid = dictId
            )

            val response = networkClient.doRequest(request)
            when (response.resultCode) {
                -1 -> DataUpdateResult.NoConnection
                200 -> {
                    database.libraryDao()
                        .insertWords((response as DictionaryResponse).words.map { it.toEntity() })
                    (response as DictionaryResponse).translate.forEach {
                        val x = it
                    }
//                    database.libraryDao()
//                        .insertTranslate((response as DictionaryResponse).translate.map { it.toEntity() })
                    database.libraryDao().insertWordInList(
                        (response as DictionaryResponse).words.map {
                            WordInDictEntity(
                                wordId = it.id,
                                dictId = dictId,
                            )
                        }
                    )
                    DataUpdateResult.DataUpdated
                }

                401 -> {
                    settings.userKey = ""
                    settings.userId = 0
                    DataUpdateResult.Error("Auth error from library")
                }

                else -> DataUpdateResult.Error("Update error: ${response.resultCode}")
            }
        }
    }

    suspend fun syncProgress(): DataUpdateResult {
        if (settings.userKey.isEmpty() || settings.userId == 0) {
            return DataUpdateResult.NotSignedIn
        }

        val lastProgress = database.libraryDao().getProgressVersion(settings.userId)
        val progressVersion = if (lastProgress.isEmpty()) 0L else lastProgress.first()
        val localProgress = database.libraryDao().getLocalProgress(settings.userId)

        val request = ProgressRequest(
            userid = settings.userId,
            userkey = settings.userKey,
            version = progressVersion,
            progress = localProgress.map { it.toDto() }
        )

        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            -1 -> DataUpdateResult.NoConnection
            200 -> {
                database.libraryDao()
                    .insertProgress(localProgress.map { it.apply { touched = false } })
                database.libraryDao().insertProgress((response as ProgressResponse)
                    .progress.map { it.toEntity(settings.userId) })
                DataUpdateResult.Synchronized
            }

            401 -> {
                settings.userKey = ""
                settings.userId = 0
                DataUpdateResult.Error("Auth error from library")
            }

            else -> DataUpdateResult.Error("Update error: ${response.resultCode}")
        }
    }

}