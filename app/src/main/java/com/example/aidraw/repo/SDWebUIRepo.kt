package com.example.aidraw.repo

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.Bean.ResultBean
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
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
}