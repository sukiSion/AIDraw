package com.example.aidraw.repo


import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean

import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService

import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow


object SDWebUIRepo {
    private val appService: AppService by lazy {
        AppNetWork.appService
    }

    fun text2image(positionPrompt: String , negationPrompt: String , sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                setText2ImageData(
                    position = positionPrompt,
                    negation = negationPrompt
                )
                this
            }))
    }

}