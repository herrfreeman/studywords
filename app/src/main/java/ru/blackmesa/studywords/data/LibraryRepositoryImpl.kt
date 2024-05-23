package ru.blackmesa.studywords.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.db.LibraryVersions
import ru.blackmesa.studywords.data.models.AuthState
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

        val response = networkClient.doRequest(
            UpdateRequest(
                userkey = settings.userKey,
                dictversion = libraryVersions.dictsVersion,
                wordsversion = libraryVersions.wordsVersion,
                wordsindictsversion = libraryVersions.wordsInDictVersion,
                wordtranslateversion = libraryVersions.wordTranslateVersion,
            )
        )
        return when (response.resultCode) {
            -1 -> UpdateResult.NoConnection
            200 -> updateLocalData(response as UpdateResponse)
            401 -> {
                settings.userKey = ""
                settings.userId = 0
                UpdateResult.Error("Auth error from library")
            }

            else -> UpdateResult.Error("Update error: ${response.resultCode}")
        }
    }

    override suspend fun signIn(userName: String, password: String): AuthState {

        if (userName.isEmpty()) {
            return AuthState.Error("Login is empty")
        }
        if (password.isEmpty()) {
            return AuthState.Error("Password is empty")
        }

        val response = networkClient.doRequest(AuthRequest(userName, password))
        return when (response.resultCode) {
            -1 -> AuthState.NoConnection
            200 -> {
                settings.userKey = (response as AuthResponse).userkey
                settings.userId = (response as AuthResponse).userid
                delay(1500)
                AuthState.Success((response as AuthResponse).message)
            }

            401 -> {
                settings.userKey = ""
                settings.userId = 0
                AuthState.Error("Auth error")
            }

            else -> AuthState.Error("Other error: ${response.resultCode}")
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
            database.libraryDao().insertProgress(progress.map { it.toProgressEntity(settings.userId) })
        }
    }

    private suspend fun updateLocalData(updateResponse: UpdateResponse): UpdateResult {

        return withContext(Dispatchers.IO) {

            with(database.libraryDao()) {
                insertWords(updateResponse.words.map { it.toWordEntity() })
                insertWordInList(updateResponse.wordsindicts.map { it.toWordInDictEntity() })
                insertTranslate(updateResponse.wordtranslate.map { it.toWordTranslateEntity() })

                if (updateResponse.dictionaries.isEmpty()) {
                    UpdateResult.Synchronized
                } else {
                    insertDict(updateResponse.dictionaries.map { it.toDictEntity() })
                    UpdateResult.LibraryUpdated(
                        getDict().map { it.toDictionary() })
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

            LibraryVersions(
                dictsVersion = if (dict.isEmpty()) 0L else dict.first(),
                wordsVersion = if (words.isEmpty()) 0L else words.first(),
                wordsInDictVersion = if (wordindict.isEmpty()) 0L else wordindict.first(),
                wordTranslateVersion = if (translate.isEmpty()) 0L else translate.first(),
            )
        }
    }

}