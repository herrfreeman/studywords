package ru.blackmesa.studywords

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.blackmesa.studywords.data.dataModule
import ru.blackmesa.studywords.domain.AnaliticsInteractor
import ru.blackmesa.studywords.domain.SettingsInteractor
import ru.blackmesa.studywords.domain.domainModule
import ru.blackmesa.studywords.ui.uiModule2

class StudyWordsApp : Application() {

    //lateinit var appSettings: AppSettings
    val settings: SettingsInteractor by inject()
    val analitics: AnaliticsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

//        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
//        firebaseAnalytics.logEvent("app_started", null)
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)

        startKoin {
            androidContext(this@StudyWordsApp)
            modules(
                dataModule,
                domainModule,
                uiModule2,
            )
        }

        analitics.logEvent(FirebaseAnalytics.Event.APP_OPEN)
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