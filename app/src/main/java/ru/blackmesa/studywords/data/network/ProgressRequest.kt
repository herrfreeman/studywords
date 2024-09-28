package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.ProgressDto

data class ProgressRequest(
    val userid: Int,
    val userkey: String,
    val version: Long,
    val progress: List<ProgressDto>
)