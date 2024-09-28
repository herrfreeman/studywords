package ru.blackmesa.studywords.domain

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.crashlytics

class AnaliticsInteractorImpl(context: Context) : AnaliticsInteractor {

    val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logEvent(event: String, message: String) {
        var bundle: Bundle? = null
        if (message.isNotEmpty()) {
            bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT, message)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
        firebaseAnalytics.logEvent(event, null)
        Log.d("STUDY_WORDS", "Event: $event $message")
    }

    override fun logError(message: String) {
        Firebase.crashlytics.log(message)
        Log.d("STUDY_WORDS", "Error: $message")
    }


}