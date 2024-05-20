package ru.blackmesa.studywords.data.network


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StudyWordsApi {

    @POST("/auth")
    @Headers("Content-Type: application/json")
    suspend fun authenticate(
        @Body request: AuthRequest,
    ): AuthResponse

    @POST("/update")
    @Headers("Content-Type: application/json")
    suspend fun update(
        @Body request: UpdateRequest,
    ): UpdateResponse
}