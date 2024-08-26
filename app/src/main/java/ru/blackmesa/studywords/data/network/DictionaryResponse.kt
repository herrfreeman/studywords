package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto

data class DictionaryResponse(
    val dictionaries: List<WordDto>,
    val translate: List<WordTranslateDto>,
) : Response()