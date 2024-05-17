package ru.blackmesa.studywords.data.network

import retrofit2.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): String
}