package ru.blackmesa.studywords.data

import ru.blackmesa.studywords.data.models.AuthState
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Progress
import ru.blackmesa.studywords.data.models.UpdateResult
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.domain.AuthResult
import ru.blackmesa.studywords.domain.CreateUserResult
import java.sql.Timestamp

interface AuthRepository {

    suspend fun signIn(userName: String, password: String): AuthResult
    suspend fun createUser(userName: String): CreateUserResult
}