package com.example.aidraw.net

import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean
import retrofit2.http.Body
import retrofit2.http.POST

interface AppService {

    @POST(ApiUrl.getPic)
    suspend fun getPhoto(@Body requestBean: RequestBean): ResultBean
}