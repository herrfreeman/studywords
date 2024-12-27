package ru.blackmesa.studywords.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration


class RetrofitNetworkClient(
    private val context: Context
) : NetworkClient {

//    private val baseUrl = "https://d5dj1kvquhb9ngfbcamr.apigw.yandexcloud.net"
    private val baseUrl = "https://herrfreeman.pythonanywhere.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(createDefaultOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val webService = retrofit.create(StudyWordsApi::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            when (dto) {

                is AuthRequest -> {
                    try {
                        webService.authenticate(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        //val errorMessage = e.response()?.errorBody()?.string() ?: ""
                        //Log.d("STUDY_WORDS_DEBUG", "HTTP error: ${e.code()} message: ${errorMessage}")
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }

                }

                is CreateRestoreRequest -> {
                    try {
                        webService.createUser(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }

                }

                is ConfirmRequest -> {
                    try {
                        webService.confirm(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }

                }

                is LibraryRequest -> {
                    try {
                        webService.library(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }

                is DictionaryRequest -> {
                    try {
                        webService.dictionary(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }

                is ProgressRequest -> {
                    try {
                        webService.progress(dto).apply { resultCode = 200 }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = e.code()
                            errorMessage = e.response()?.errorBody()?.string() ?: ""
                        }
                    } catch (e: Throwable) {
                        Response().apply { resultCode = 500 }
                    }
                }

                else -> Response().apply { resultCode = 400 }
            }
        }

    }

    private fun createDefaultOkHttpClient(): OkHttpClient {
        //val interceptor = HttpLoggingInterceptor()
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(30))
            .writeTimeout(Duration.ofSeconds(30))
            //.addInterceptor(interceptor)
            .build()
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

}