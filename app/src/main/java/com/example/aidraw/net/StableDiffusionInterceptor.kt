package com.example.aipainter.logic.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class StableDiffusionInterceptor:  Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader("Accept", "*/*")
            .addHeader("Authorization", "token ")
            .build()
        return chain.proceed(request)
    }
}