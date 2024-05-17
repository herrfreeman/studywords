package ru.blackmesa.studywords.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitNetworkClient(
    private val context: Context
) : NetworkClient {

    private val baseUrl = "https://d5dj1kvquhb9ngfbcamr.apigw.yandexcloud.net"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(createDefaultOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val webService = retrofit.create(StudyWordsApi::class.java)

    override suspend fun doRequest(dto: Any): String {
        if (isConnected() == false) {
            return "Not connected"
        }

        return try {
            withContext(Dispatchers.IO) {

                when (dto) {
                    is AuthRequest -> {
                        val result = webService.authenticate(dto)
                        result.body()?.message?: "fail"

                    }
                    else -> "Wrong FTO"

                }
            }
        } catch (e: Throwable) {
            "Got exception: ${e.message}"
        }

    }

    private fun createDefaultOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
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