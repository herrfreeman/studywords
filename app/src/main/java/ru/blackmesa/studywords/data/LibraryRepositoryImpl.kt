package ru.blackmesa.studywords.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.blackmesa.studywords.data.db.AppDatabase
import ru.blackmesa.studywords.data.db.LibraryVersions
import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordInDict
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
        Log.d("STUDY_WORDS_DEBUG", "Make update")

        val userKey = settings.getSettings().userKey
        if (userKey.isEmpty()) {
            return UpdateResult.NotSignedIn
        }

        val libraryVersions = getLibraryVersions()

        val response = networkClient.doRequest(UpdateRequest(
            userkey = userKey,
            dictversion = libraryVersions.dictsVersion,
            wordsversion = libraryVersions.wordsVersion,
            wordsindictsversion = libraryVersions.wordsInDictVersion,
            wordtranslateversion = libraryVersions.wordTranslateVersion,
        ))
        return when (response.resultCode) {
            -1 -> UpdateResult.NoConnection
            200 -> updateLocalData(response as UpdateResponse)
            401 -> {
                settings.setSettings(settings.getSettings().apply { this.userKey = "" })
                UpdateResult.Error("Auth error from library")
            }

            else -> UpdateResult.Error("Update error: ${response.resultCode}")
        }
    }

    override suspend fun signIn(): AuthState {
        Log.d("STUDY_WORDS_DEBUG", "Sign in")

        val userName = settings.getSettings().userName
        val password = settings.getSettings().password

        if (userName.isEmpty()) {
            return AuthState.Error("Login is empty")
        }
        if (password.isEmpty()) {
            return AuthState.Error("Password is empty")
        }

        val response = networkClient.doRequest(AuthRequest(userName, password))
        Log.d("STUDY_WORDS_DEBUG", "Got auth: $response")
        return when (response.resultCode) {
            -1 -> AuthState.NoConnection
            200 -> {
                settings.setSettings(settings.getSettings().apply {
                    userKey = (response as AuthResponse).userkey
                })
                AuthState.Success
            }

            401 -> {
                settings.setSettings(settings.getSettings().apply { userKey = "" })
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

    override suspend fun getWords(dictId: Int): List<WordInDict> {
        return withContext(Dispatchers.IO) {
            Log.d("STUDY_WORDS_DEBUG", "Load dict id = $dictId")
            val dictdata = database.libraryDao().getWordsInDict(dictId)
            Log.d("STUDY_WORDS_DEBUG", "Dict data = $dictdata")
            dictdata.map { it.toWordInDict() }
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