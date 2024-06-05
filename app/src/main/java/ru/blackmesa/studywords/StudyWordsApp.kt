package ru.blackmesa.studywords

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.blackmesa.studywords.data.dataModule
import ru.blackmesa.studywords.data.models.AppSettings
import ru.blackmesa.studywords.domain.SettingsInteractor
import ru.blackmesa.studywords.domain.domainModule
import ru.blackmesa.studywords.ui.uiModule2

class StudyWordsApp : Application() {

    //lateinit var appSettings: AppSettings
    val settings: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StudyWordsApp)
            modules(
                dataModule,
                domainModule,
                uiModule2,
            )
        }

        //appSettings = settingsInteractor
        updateTheme()
    }

    fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (settings.nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
                //AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

}