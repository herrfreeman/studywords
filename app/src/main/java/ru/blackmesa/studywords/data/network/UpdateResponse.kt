package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.DictionaryDto

data class UpdateResponse (
    val errormessage: String,
    val dictionaries: List<DictionaryDto>
) : Response()