package ru.blackmesa.studywords.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.db.LibraryVersions
import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.data.network.AuthRequest
import ru.blackmesa.studywords.data.network.AuthResponse
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.network.UpdateRequest
import ru.blackmesa.studywords.data.network.UpdateResponse
import ru.blackmesa.studywords.data.settings.SettingsRepository

class LibraryRepositoryImpl(
    private val context: Context,
    private val settings: SettingsRepository,
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
) : LibraryRepository {

    override suspend fun updateAllData(): UpdateResult {

        if (settings.userKey.isEmpty() || settings.userId == 0) {
            return UpdateResult.NotSignedIn
        }

        val libraryVersions = getLibraryVersions()
        val request = UpdateRequest(
            userkey = settings.userKey,
            dictversion = libraryVersions.dictsVersion,
            wordsversion = libraryVersions.wordsVersion,
            wordsindictsversion = libraryVersions.wordsInDictVersion,
            wordtranslateversion = libraryVersions.wordTranslateVersion,
            progressversion = libraryVersions.progressVersion,
            localprogress = getLocalProgress(settings.userId)
        )

        Log.d("STUDY_WORDS", Gson().toJson(request))

        val response = networkClient.doRequest(request)
        return when (response.resultCode) {
            -1 -> UpdateResult.NoConnection
            200 -> updateLocalData(request, response as UpdateResponse)
            401 -> {
                settings.userKey = ""
                settings.userId = 0
                UpdateResult.Error("Auth error from library")
            }

            else -> UpdateResult.Error("Update error: ${response.resultCode}")
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
                .insertProgress(progress.map { it.toProgressEntity(settings.userId) })
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

    private suspend fun updateLocalData(
        updateRequest: UpdateRequest,
        updateResponse: UpdateResponse
    ): UpdateResult {

        return withContext(Dispatchers.IO) {

            with(database.libraryDao()) {
                insertWords(updateResponse.words.map { it.toWordEntity() })
                insertWordInList(updateResponse.wordsindicts.map { it.toWordInDictEntity() })
                insertTranslate(updateResponse.wordtranslate.map { it.toWordTranslateEntity() })
                //At first update sent progress as untouched
                insertProgress(updateRequest.localprogress.map {
                    it.toProgressEntity(settings.userId, false)
                })
                //Then load external progress data
                insertProgress(updateResponse.progress.map {
                    it.toProgressEntity(settings.userId, false)
                })

                if (updateResponse.dictionaries.isEmpty()
                    && updateResponse.words.isEmpty()
                    && updateResponse.wordsindicts.isEmpty()
                    && updateResponse.wordtranslate.isEmpty()
                    && updateResponse.progress.isEmpty()
                ) {
                    UpdateResult.Synchronized
                } else {
                    insertDict(updateResponse.dictionaries.map { it.toDictEntity() })
                    UpdateResult.LibraryUpdated(
                        getDictionariesWithProgress()
                    )
                }
            }
        }
    }

    private suspend fun getLibraryVersions(): LibraryVersions {
        return withContext(Dispatchers.IO) {
            val dict = database.libraryDao().getDictVersion()
            val words = database.libraryDao().getWordsVersion()
            val wordindict = database.libraryDao().getWordInDictVersion()
            val translate = database.libraryDao().getTranslateVersion()
            val progress = database.libraryDao().getProgressVersion(settings.userId)

            LibraryVersions(
                dictsVersion = if (dict.isEmpty()) 0L else dict.first(),
                wordsVersion = if (words.isEmpty()) 0L else words.first(),
                wordsInDictVersion = if (wordindict.isEmpty()) 0L else wordindict.first(),
                wordTranslateVersion = if (translate.isEmpty()) 0L else translate.first(),
                progressVersion = if (progress.isEmpty()) 0L else progress.first(),
            )
        }
    }

    private suspend fun getLocalProgress(userId: Int): List<ProgressDto> {
        return withContext(Dispatchers.IO) {
            database.libraryDao().getLocalProgress(userId).map { it.toProgressDto() }
        }
    }

}