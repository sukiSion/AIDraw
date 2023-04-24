package com.example.aidraw.net

object AppNetWork {
    val appService: AppService = RetrofitClient.instance.create()
}