package ru.blackmesa.studywords.data.network


import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StudyWordsApi {

    @POST("/authenticate")
    @Headers("Content-Type: application/json")
    suspend fun authenticate(
        @Body request: AuthRequest,
    ): AuthResponse

    @POST("/createuser")
    @Headers("Content-Type: application/json")
    suspend fun createUser(
        @Body request: CreateUserRequest,
    ): CreateUserResponse

    @POST("/createuserconfirm")
    @Headers("Content-Type: application/json")
    suspend fun confirm(
        @Body request: ConfirmRequest,
    ): ConfirmResponse

    @POST("/update")
    @Headers("Content-Type: application/json")
    suspend fun update(
        @Body request: UpdateRequest,
    ): UpdateResponse
}