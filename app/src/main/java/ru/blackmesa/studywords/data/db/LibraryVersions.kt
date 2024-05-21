package ru.blackmesa.studywords.data.db

data class LibraryVersions(
    val dictsVersion: Long,
    val wordsVersion: Long,
    val wordsInDictVersion: Long,
    val wordTranslateVersion: Long,
)