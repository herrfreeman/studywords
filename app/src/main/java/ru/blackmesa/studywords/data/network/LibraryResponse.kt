package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.DictionaryDto

data class LibraryResponse(
    val dictionaries: List<DictionaryDto>,
) : Response()