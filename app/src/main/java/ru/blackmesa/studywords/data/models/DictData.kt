package ru.blackmesa.studywords.data.models

data class DictData(
    val id: Int,
    val name: String,
    val total: Int,
    val repeat: Int,
    val wait: Int,
    val done: Int,
)