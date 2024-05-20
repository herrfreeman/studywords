package ru.blackmesa.studywords.data.network

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}