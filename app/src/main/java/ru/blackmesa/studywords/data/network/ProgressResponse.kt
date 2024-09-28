package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto

data class ProgressResponse(
    val progress: List<ProgressDto>,
) : Response()