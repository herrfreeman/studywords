package ru.blackmesa.studywords.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.db.LibraryVersions
import ru.blackmesa.studywords.data.db.WordInDictEntity
import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.LibraryUpdateResult
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.data.network.DictionaryRequest
import ru.blackmesa.studywords.data.network.DictionaryResponse
import ru.blackmesa.studywords.data.network.LibraryRequest
import ru.blackmesa.studywords.data.network.LibraryResponse
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.settings.SettingsRepository

class LibraryRepositoryImpl(
    private val context: Context,
    private val settings: SettingsRepository,
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) : LibraryRepository {

    override suspend fun updateLibraryFromServer(): LibraryUpdateResult {

        if (settings.userKey.isEmpty() || settings.userId == 0) {
            return LibraryUpdateResult.NotSignedIn
        }

        val libraryVersions = getLibraryVersions()
        val request = LibraryRequest(
            userid = settings.userId,
            userkey = settings.userKey,
            version = libraryVersions.dictsVersion,
        )

        Log.d("STUDY_WORDS", Gson().toJson(request))

        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            -1 -> LibraryUpdateResult.NoConnection
            200 -> updateLocalLibrary(response as LibraryResponse)
            401 -> {
                settings.userKey = ""
                settings.userId = 0
                LibraryUpdateResult.Error("Auth error from library")
            }

            else -> LibraryUpdateResult.Error("Update error: ${response.resultCode}")
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

    private suspend fun updateLocalLibrary(
        libraryResponse: LibraryResponse
    ): LibraryUpdateResult {
        return withContext(Dispatchers.IO) {
            //TODO сделать обновление словарей в транзакции

            val updatedDictionaries = libraryResponse.dictionaries
            if (updatedDictionaries.isEmpty()) {
                LibraryUpdateResult.Synchronized
            } else {

                updatedDictionaries.forEach {
                    val dictUpdateResult = updateDictionary(it.id)
                    if (updateDictionary(it.id) != LibraryUpdateResult.LibraryUpdated) {
                        return@withContext dictUpdateResult
                    }
                }

                database.libraryDao().insertDict(updatedDictionaries.map { it.toEntity() })
                LibraryUpdateResult.LibraryUpdated
            }
        }
    }

    private suspend fun updateDictionary(dictId: Int): LibraryUpdateResult {
        val request = DictionaryRequest(
            userid = settings.userId,
            userkey = settings.userKey,
            dictid = dictId
        )

        Log.d("STUDY_WORDS", Gson().toJson(request))

        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            -1 -> LibraryUpdateResult.NoConnection
            200 -> updateLocalDictionary(dictId, response as DictionaryResponse)
            401 -> {
                settings.userKey = ""
                settings.userId = 0
                LibraryUpdateResult.Error("Auth error from library")
            }

            else -> LibraryUpdateResult.Error("Update error: ${response.resultCode}")
        }
    }

    private fun updateLocalDictionary(
        dictId: Int,
        dictionaryResponse: DictionaryResponse
    ): LibraryUpdateResult {


        database.libraryDao().insertWords(dictionaryResponse.words.map { it.toEntity() })
        database.libraryDao().insertTranslate(dictionaryResponse.translate.map { it.toEntity() })

        database.libraryDao().insertWordInList(
            dictionaryResponse.words.map {
                WordInDictEntity(
                    wordId = it.id,
                    userId = it.userId,
                    dictId = dictId,
                )
            }
        )

        return LibraryUpdateResult.LibraryUpdated
    }

//            with(database.libraryDao()) {
//
//
//                insertWords(updateResponse.words.map { it.toWordEntity() })
//                insertWordInList(updateResponse.wordsindicts.map { it.toWordInDictEntity() })
//                insertTranslate(updateResponse.wordtranslate.map { it.toWordTranslateEntity() })
//                //At first update sent progress as untouched
//                insertProgress(updateRequest.localprogress.map {
//                    it.toProgressEntity(settings.userId, false)
//                })
//                //Then load external progress data
//                insertProgress(updateResponse.progress.map {
//                    it.toProgressEntity(settings.userId, false)
//                })
//
//                if (updateResponse.dictionaries.isEmpty()
//                    && updateResponse.words.isEmpty()
//                    && updateResponse.wordsindicts.isEmpty()
//                    && updateResponse.wordtranslate.isEmpty()
//                    && updateResponse.progress.isEmpty()
//                ) {
//                    UpdateResult.Synchronized
//                } else {
//                    insertDict(updateResponse.dictionaries.map { it.toDictEntity() })
//                    UpdateResult.LibraryUpdated(
//                        getDictionariesWithProgress()
//                    )
//                }
//            }
//        }
//
//    }


//    private suspend fun updateLocalData(
//        updateRequest: UpdateRequest,
//        updateResponse: UpdateResponse
//    ): UpdateResult {
//
//        return withContext(Dispatchers.IO) {
//
//            with(database.libraryDao()) {
//                insertWords(updateResponse.words.map { it.toWordEntity() })
//                insertWordInList(updateResponse.wordsindicts.map { it.toWordInDictEntity() })
//                insertTranslate(updateResponse.wordtranslate.map { it.toWordTranslateEntity() })
//                //At first update sent progress as untouched
//                insertProgress(updateRequest.localprogress.map {
//                    it.toProgressEntity(settings.userId, false)
//                })
//                //Then load external progress data
//                insertProgress(updateResponse.progress.map {
//                    it.toProgressEntity(settings.userId, false)
//                })
//
//                if (updateResponse.dictionaries.isEmpty()
//                    && updateResponse.words.isEmpty()
//                    && updateResponse.wordsindicts.isEmpty()
//                    && updateResponse.wordtranslate.isEmpty()
//                    && updateResponse.progress.isEmpty()
//                ) {
//                    UpdateResult.Synchronized
//                } else {
//                    insertDict(updateResponse.dictionaries.map { it.toDictEntity() })
//                    UpdateResult.LibraryUpdated(
//                        getDictionariesWithProgress()
//                    )
//                }
//            }
//        }
//    }

    private suspend fun getLibraryVersions(): LibraryVersions {
        return withContext(Dispatchers.IO) {
            val library = database.libraryDao().getDictVersion()
            val progress = database.libraryDao().getProgressVersion(settings.userId)

            LibraryVersions(
                dictsVersion = if (library.isEmpty()) 0L else library.first(),
                progressVersion = if (progress.isEmpty()) 0L else progress.first(),
            )
        }
    }

    private suspend fun getLocalProgress(userId: Int): List<ProgressDto> {
        return withContext(Dispatchers.IO) {
            //database.libraryDao().getLocalProgress(userId).map { it.toProgressDto() }
            emptyList()
        }
    }

}