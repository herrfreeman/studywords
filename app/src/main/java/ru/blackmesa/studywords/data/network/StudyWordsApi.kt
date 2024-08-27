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

    @POST("/library")
    @Headers("Content-Type: application/json")
    suspend fun library(
        @Body request: LibraryRequest,
    ): LibraryResponse

    @POST("/dictionary")
    @Headers("Content-Type: application/json")
    suspend fun dictionary(
        @Body request: DictionaryRequest,
    ): DictionaryResponse

    @POST("/progress")
    @Headers("Content-Type: application/json")
    suspend fun progress(
        @Body request: ProgressRequest,
    ): ProgressResponse

}