package com.example.aidraw.net

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {

    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(ApiUrl.baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().disableHtmlEscaping().create()
                )
            )
            .build()
    }

    companion object {
        val instance: RetrofitClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitClient() }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val DEFAULT_TIMEOUT = 1000L * 60L
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(level = HttpLoggingInterceptor.Level.BODY))
            .retryOnConnectionFailure(true)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
        return okHttpClient
    }

    fun <T> create(serviceClass: Class<T>) = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}