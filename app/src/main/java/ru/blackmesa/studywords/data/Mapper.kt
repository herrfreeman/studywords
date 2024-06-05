package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.db.DictEntity
import ru.blackmesa.studywords.data.db.ProgressEntity
import ru.blackmesa.studywords.data.db.WordEntity
import ru.blackmesa.studywords.data.db.WordInDictEntity
import ru.blackmesa.studywords.data.db.WordTranslateEntity
import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordInDictDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordInDict
import ru.blackmesa.studywords.data.models.WordTranslate

fun DictEntity.toDictDto() = DictionaryDto(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun DictEntity.toDictionary() = Dictionary(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun DictionaryDto.toDictEntity() = DictEntity(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun DictionaryDto.toDictionary() = Dictionary(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun Dictionary.toDictDto() = DictionaryDto(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun Dictionary.toDictEntity() = DictEntity(
    id = id,
    name = name,
    orderfield = orderfield,
    version = version,
)

fun WordEntity.toWordDto() = WordDto(
    id = id,
    word = word,
    version = version,
)

fun WordEntity.toWord() = Word(
    id = id,
    word = word,
    version = version,
)

fun WordDto.toWordEntity() = WordEntity(
    id = id,
    word = word,
    version = version,
)

fun WordDto.toWord() = Word(
    id = id,
    word = word,
    version = version,
)

fun Word.toWordEntity() = WordEntity(
    id = id,
    word = word,
    version = version,
)

fun Word.toWordDto() = WordDto(
    id = id,
    word = word,
    version = version,
)

fun WordInDictEntity.toWordInDictDto() = WordInDictDto(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordInDictEntity.toWordInDict() = WordInDict(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordInDict.toWordInDictDto() = WordInDictDto(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordInDict.toWordInDictEntity() = WordInDictEntity(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordInDictDto.toWordInDictEntity() = WordInDictEntity(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordInDictDto.toWordInDict() = WordInDict(
    wordid = wordid,
    dictid = dictid,
    version = version,
)

fun WordTranslateEntity.toWordTranslateDto() = WordTranslateDto(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun WordTranslateEntity.toWordTranslate() = WordTranslate(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun WordTranslate.toWordTranslateDto() = WordTranslateDto(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun WordTranslate.toWordTranslateEntity() = WordTranslateEntity(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun WordTranslateDto.toWordTranslateEntity() = WordTranslateEntity(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun WordTranslateDto.toWordTranslate() = WordTranslate(
    id = id,
    wordid = wordid,
    translate = translate,
    version = version,
)

fun ProgressEntity.toProgressDto() = ProgressDto(
    wordid = wordid,
    status = status,
    repeatdate = repeatdate,
    version = version,
)

fun ProgressEntity.toProgress() = Progress(
    wordid = wordid,
    status = status,
    repeatdate = repeatdate,
    version = version,
    touched = touched,
)

fun Progress.toProgressDto(userid: Int) = ProgressDto(
    wordid = wordid,
    status = status,
    repeatdate = repeatdate,
    version = version,
)

fun Progress.toProgressEntity(userid: Int) = ProgressEntity(
    wordid = wordid,
    userid = userid,
    status = status,
    repeatdate = repeatdate,
    version = version,
    touched = touched,
)

fun ProgressDto.toProgressEntity(userid: Int, touched: Boolean) = ProgressEntity(
    wordid = wordid,
    userid = userid,
    status = status,
    repeatdate = repeatdate,
    version = version,
    touched = touched,
)

fun ProgressDto.toProgress(touched: Boolean) = Progress(
    wordid = wordid,
    status = status,
    repeatdate = repeatdate,
    version = version,
    touched = touched,
)