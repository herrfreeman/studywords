package ru.blackmesa.studywords.data.models

data class DictData(
    val id: Int,
    val name: String,
    val totalCount: Int,
    val repeatCount: Int,
    val waitCount: Int,
    val doneCount: Int,
)