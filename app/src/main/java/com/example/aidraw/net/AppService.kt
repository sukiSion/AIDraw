package com.example.aidraw.net

import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface AppService {

    @POST(ApiUrl.sdUrl)
    suspend fun sdWebUINetWork(@Body requestBean: RequestBean): ResultBean

    @Streaming
    @GET
    suspend fun downloadImage(@Url url: String) : ResponseBody
}