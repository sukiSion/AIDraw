package com.example.aidraw.repo


import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean

import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService
import com.example.aidraw.pool.ConstantPool

import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.flow
import java.util.Base64


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

    fun getImageInformation(
        imageBase64: String,
        sessionHash: String
    ) = flow {
        emit(appService.sdWebUINetWork(RequestBean(
            session_hash = sessionHash
        ).run {
             setGetImageInformation(imageBase64)
            this
        }))
    }

    fun image2image(positionPrompt: String , negationPrompt: String , imageBase64: String ,sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                setImage2ImageData(
                    position = positionPrompt,
                    negation = negationPrompt,
                    imageBase64 = "${ConstantPool.image_base64_scheme}${imageBase64}"
                )
                this
            }))
    }

    fun clipReversePrompt(imageBase64: String , sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                setClipReversePrompt(imageBase64)
                this
            }))
    }

    fun deepBooruReversePrompt(imageBase64: String , sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                setDeepBooruReversePrompt(imageBase64)
                this
            }))
    }

    fun changeModel(model:String , sessionHash: String): Flow<ResultBean>
    = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                setChangeModelData(model)
                this
            }
        ))
    }

    fun getSupportModels(sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(
                data = listOf<Any?>().toTypedArray(),
                fn_index = ConstantPool.fn_index_support_models,
                session_hash = sessionHash,
            )
        ))
    }

    fun initApp(sessionHash: String):Flow<ResultBean>
    = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(
                data = listOf<Any?>().toTypedArray(),
                fn_index = ConstantPool.fn_index_init_sd_web_ui,
                session_hash = sessionHash,
            )
        ))
    }



}