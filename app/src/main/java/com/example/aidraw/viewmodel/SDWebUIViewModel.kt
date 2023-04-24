package com.example.aidraw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidraw.Bean.OutPutBean
import com.example.aidraw.Bean.ResultBean
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.net.ApiUrl
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.repo.SDWebUIRepo
import com.example.aidraw.state.SDWebUIConfigState
import com.example.aidraw.state.SDWebUICreateState
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SDWebUIViewModel : ViewModel(){

    // SdWebUI事件发送和处理管道
    private val SDWebUIInentChannel:Channel<SDWebUICreateIntent> = Channel(Channel.UNLIMITED)

    // SdWebUI结果状态发送和处理管理
    private val _SDWebUICreateState:MutableStateFlow<SDWebUICreateState?> = MutableStateFlow(null)
    val sdWebUICreateState : StateFlow<SDWebUICreateState?> = _SDWebUICreateState.asStateFlow()

    init {
        viewModelScope.launch(context = viewModelScope.coroutineContext + AppCoroutineExceptionHandler()) {
            SDWebUIInentChannel.consumeAsFlow().collect{
               when(it){
                   is SDWebUICreateIntent.text2Image -> {
                       text2image(
                           positionPrompt = it.positionPrompt,
                           navigationPrompt = it.navigationPrompt,
                           seesionHash = it.seesionHash
                       )
                   }
               }
            }
        }
    }

    fun post(intent: SDWebUICreateIntent){
        viewModelScope.launch {
            SDWebUIInentChannel.send(intent)
        }
    }

    private suspend fun text2image(positionPrompt: String , navigationPrompt: String , seesionHash: String){
        SDWebUIRepo.text2image(
            positionPrompt = positionPrompt,
            navigationPrompt = navigationPrompt,
            sessionHash = seesionHash
        )
            .flowOn(Dispatchers.IO)
            .collect{
                handlet2iResultBean(resultBean = it)?.let {
                    withContext(Dispatchers.Main){
                        _SDWebUICreateState.emit(SDWebUICreateState.text2imageResult(
                            ApiUrl.resultUrl + it.name
                        )
                        )
                    }
                }
            }
    }


    // 文生图结果解析
    private inline fun handlet2iResultBean(resultBean: ResultBean): OutPutBean?{
        resultBean.data.forEach {
            if(it is List<*>){
                it.forEach {
                   it?.let {
                       if(it is LinkedTreeMap<*, *>){
                           val outPutBean = Gson().fromJson(Gson().toJson(it) , OutPutBean::class.java)
                           return outPutBean
                       }
                   }
                }
            }
        }
        return null
    }

}