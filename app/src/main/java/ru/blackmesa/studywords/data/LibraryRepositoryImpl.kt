package ru.blackmesa.studywords.data

import android.content.Context
import android.util.Log
import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.network.NetworkClient
import ru.blackmesa.studywords.data.network.UpdateRequest
import ru.blackmesa.studywords.data.network.UpdateResponse
import ru.blackmesa.studywords.data.settings.SettingsRepository

class LibraryRepositoryImpl(
    private val context: Context,
    private val settings: SettingsRepository,
    private val networkClient: NetworkClient,
) : LibraryRepository {

    override suspend fun updateAllData(): UpdateResult {
        Log.d("STUDY_WORDS_DEBUG", "Make update")

        val userKey = settings.getSettings().userKey
        if (userKey.isEmpty()) {
            return UpdateResult.NotSignedIn
        }
        val response = networkClient.doRequest(UpdateRequest(userKey, 0))
        return when (response.resultCode) {
            -1 -> UpdateResult.NoConnection
            200 -> {
                updateLocalData((response as UpdateResponse).dictionaries)
                UpdateResult.Synchronized
            }
            else -> UpdateResult.Error("Update error: ${response.resultCode}")
        }
    }

    private fun updateLocalData(dictionaries: List<DictionaryDto>) {
        Log.d("STUDY_WORDS_DEBUG" ,"Got data: $dictionaries")
    }

}