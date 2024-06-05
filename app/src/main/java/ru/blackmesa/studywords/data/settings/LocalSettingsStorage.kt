package ru.blackmesa.studywords.data.settings

import android.content.Context
import com.google.gson.Gson
import ru.blackmesa.studywords.data.models.AppSettings
import ru.blackmesa.studywords.data.models.Credentials


class LocalSettingsStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    private val gson = Gson()

    private companion object {
        const val APP_SETTINGS = "APP_SETTINGS"
        const val APP_CREDENTIALS = "APP_CREDENTIALS"
    }

    fun getSettings(): AppSettings {
        val storedSettings: AppSettings? = gson.fromJson(
            sharedPreferences.getString(APP_SETTINGS, ""),
            AppSettings::class.java
        )
        return storedSettings ?: AppSettings()
    }

    fun setSettings(settings: AppSettings) {
        sharedPreferences
            .edit()
            .putString(APP_SETTINGS, gson.toJson(settings))
            .apply()
    }

    fun getCredentials(): Credentials {
        val credentials: Credentials? = gson.fromJson(
            sharedPreferences.getString(APP_CREDENTIALS, ""),
            Credentials::class.java
        )
        return credentials ?: Credentials()
    }

    fun setCredentials(credentials: Credentials) {
        sharedPreferences
            .edit()
            .putString(APP_CREDENTIALS, gson.toJson(credentials))
            .apply()
    }

}