package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.ProgressDto

data class UpdateRequest(
    val userkey: String,
    val dictversion: Long,
    val progressversion: Long,
    val localprogress: List<ProgressDto>
)