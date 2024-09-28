package ru.blackmesa.studywords.domain

interface AnaliticsInteractor {

    fun logEvent(event: String, message: String = "")
    fun logError(message: String)

}