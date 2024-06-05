package ru.blackmesa.studywords.data.network

import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordInDictDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto

data class UpdateResponse (
    val errormessage: String,
    val dictionaries: List<DictionaryDto>,
    val words: List<WordDto>,
    val wordsindicts: List<WordInDictDto>,
    val wordtranslate: List<WordTranslateDto>,
    val progress: List<ProgressDto>,
) : Response()