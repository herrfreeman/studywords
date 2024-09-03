package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.db.DictEntity
import ru.blackmesa.studywords.data.db.ProgressEntity
import ru.blackmesa.studywords.data.db.WordEntity
import ru.blackmesa.studywords.data.db.WordTranslateEntity
import ru.blackmesa.studywords.data.dto.DictionaryDto
import ru.blackmesa.studywords.data.dto.ProgressDto
import ru.blackmesa.studywords.data.dto.WordDto
import ru.blackmesa.studywords.data.dto.WordTranslateDto
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress

fun DictEntity.toDto() = DictionaryDto(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictEntity.toDictionary() = Dictionary(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictionaryDto.toEntity() = DictEntity(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun DictionaryDto.toDictionary() = Dictionary(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun Dictionary.toDto() = DictionaryDto(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun Dictionary.toEntity() = DictEntity(
    id = id,
    name = name,
    orderField = orderField,
    version = version,
    parentId = parentId,
    isFolder = isFolder,
    isDefault = isDefault,
)

fun WordDto.toEntity() = WordEntity(
    id = id,
    word = word,
)

fun WordTranslateDto.toEntity() = WordTranslateEntity(
    id = id,
    wordId = wordId,
    translate = translate,
)

fun Progress.toEntity(userId: Int) = ProgressEntity(
    wordId = wordId,
    userId = userId,
    status = status,
    repeatDate = repeatDate,
    version = version,
    touched = touched,
)

fun ProgressEntity.toDto() = ProgressDto(
    wordId = wordId,
    status = status,
    version = version,
    repeatDate = repeatDate,
)

fun ProgressDto.toEntity(userId: Int) = ProgressEntity(
    wordId = wordId,
    userId = userId,
    status = status,
    repeatDate = repeatDate,
    version = version,
    touched = false,
)

