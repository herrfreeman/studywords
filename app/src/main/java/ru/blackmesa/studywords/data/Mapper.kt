package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.db.DictEntity
import ru.blackmesa.studywords.data.db.ProgressEntity
import ru.blackmesa.studywords.data.db.WordEntity
import ru.blackmesa.studywords.data.db.WordTranslateEntity
import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress

fun DictEntity.toDto() = DictionaryDto(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictEntity.toDictionary() = Dictionary(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictionaryDto.toEntity() = DictEntity(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictionaryDto.toDictionary() = Dictionary(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun Dictionary.toDto() = DictionaryDto(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun Dictionary.toEntity() = DictEntity(
    id = id,
    userId = userId,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    parentUserId = parentUserId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun WordDto.toEntity() = WordEntity(
    id = id,
    userId = userId,
    word = word,
)

fun WordTranslateDto.toEntity() = WordTranslateEntity(
    id = id,
    wordId = wordId,
    userId = userId,
    translate = translate,
)

fun Progress.toEntity(userid: Int) = ProgressEntity(
    wordId = wordId,
    wordUserId = 0,
    userId = userid,
    status = status,
    repeatDate = repeatDate,
    version = version,
    touched = touched,
)

//fun WordEntity.toWordDto() = WordDto(
//    id = id,
//    userId = userId,
//    word = word,
//)
//
//fun WordEntity.toWord() = Word(
//    id = id,
//    userId = userId,
//    word = word,
//)
//
//fun WordDto.toWordEntity() = WordEntity(
//    id = id,
//    userId = userId,
//    word = word,
//)
//
//fun WordDto.toWord() = Word(
//    id = id,
//    userId = userId,
//    word = word,
//)
//
//fun Word.toWordEntity() = WordEntity(
//    id = id,
//    userId = userId,
//    word = word,
//)
//
//fun Word.toWordDto() = WordDto(
//    id = id,
//    word = word,
//    version = version,
//)
//
//fun WordInDictEntity.toWordInDictDto() = WordInDictDto(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordInDictEntity.toWordInDict() = WordInDict(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordInDict.toWordInDictDto() = WordInDictDto(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordInDict.toWordInDictEntity() = WordInDictEntity(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordInDictDto.toWordInDictEntity() = WordInDictEntity(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordInDictDto.toWordInDict() = WordInDict(
//    wordid = wordid,
//    dictid = dictid,
//    version = version,
//)
//
//fun WordTranslateEntity.toWordTranslateDto() = WordTranslateDto(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun WordTranslateEntity.toWordTranslate() = WordTranslate(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun WordTranslate.toWordTranslateDto() = WordTranslateDto(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun WordTranslate.toWordTranslateEntity() = WordTranslateEntity(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun WordTranslateDto.toWordTranslateEntity() = WordTranslateEntity(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun WordTranslateDto.toWordTranslate() = WordTranslate(
//    id = id,
//    wordid = wordid,
//    translate = translate,
//    version = version,
//)
//
//fun ProgressEntity.toProgressDto() = ProgressDto(
//    wordid = wordid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//)
//
//fun ProgressEntity.toProgress() = Progress(
//    wordid = wordid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//    touched = touched,
//)
//
//fun Progress.toProgressDto(userid: Int) = ProgressDto(
//    wordid = wordid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//)
//
//fun Progress.toProgressEntity(userid: Int) = ProgressEntity(
//    wordid = wordid,
//    userid = userid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//    touched = touched,
//)
//
//fun ProgressDto.toProgressEntity(userid: Int, touched: Boolean) = ProgressEntity(
//    wordid = wordid,
//    userid = userid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//    touched = touched,
//)
//
//fun ProgressDto.toProgress(touched: Boolean) = Progress(
//    wordid = wordid,
//    status = status,
//    repeatdate = repeatdate,
//    version = version,
//    touched = touched,
//)