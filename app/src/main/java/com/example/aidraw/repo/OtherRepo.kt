package com.example.aidraw.repo

import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService
import kotlinx.coroutines.flow.flow

object OtherRepo {

    private val appService: AppService by lazy {
        AppNetWork.appService
    }

    fun downlaodImage(imageUrl: String) =
        flow {
          emit(appService.downloadImage(url = imageUrl))
        }

}