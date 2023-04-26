package com.example.aidraw.repo

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.aidraw.Bean.OutPutBean
import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService
import com.example.aidraw.pool.ConstantPool
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.CoroutineContext

object SDWebUIRepo {
    private val appService: AppService by lazy {
        AppNetWork.appService
    }

    fun text2image(positionPrompt: String , navigationPrompt: String , sessionHash: String): Flow<ResultBean>
            = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash = sessionHash).run {
                this.setPosition(positionPrompt)
                this.setNavigation(navigationPrompt)
                this
            }))
    }

    fun changHDRepair(
        switch: Boolean ,
        width: Int ,
        height: Int,
        adjustmentWidth: Int = 0,
        adjustmentHeight: Int = 0,
        scale: Float,
        sessionHash: String
    ):Flow<ResultBean>
    = flow {
        emit(appService.sdWebUINetWork(
            RequestBean(session_hash =  sessionHash).apply {
                this.changeHDRepire(switch)
            }
        ))
    }.flatMapConcat {
        it.data.takeIf {
            it.isNotEmpty()
        }?.let {
            if(it[0] is LinkedTreeMap<*, *>){
                flow {
                    emit(appService.sdWebUINetWork(RequestBean(
                        session_hash = sessionHash
                    ).apply {
                         this.setGenerateConfig(
                             fn_index = ConstantPool.fn_index_hd_config,
                             width = width,
                             height = height,
                             adjustmentWidth = adjustmentWidth,
                             adjustmentHeight = adjustmentHeight,
                             scale = scale,
                             openHdRepire = switch
                         )
                     }))
                }
            }
        }
        emptyFlow()
    }

}