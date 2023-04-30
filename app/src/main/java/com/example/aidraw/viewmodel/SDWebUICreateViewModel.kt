package com.example.aidraw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidraw.Bean.OutPutBean
import com.example.aidraw.Bean.ResultBean
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.net.ApiUrl
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.repo.SDWebUIRepo
import com.example.aidraw.state.SDWebUICreateState
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SDWebUICreateViewModel : ViewModel(){

    // SdWebUI事件发送和处理管道
    private var SDWebUICreateInentChannel:Channel<SDWebUICreateIntent> = Channel(Channel.UNLIMITED)

    // SdWebUI结果状态发送和处理管理
    private val _SDWebUICreateState:MutableStateFlow<SDWebUICreateState?> = MutableStateFlow(null)
    val sdWebUICreateState : StateFlow<SDWebUICreateState?> = _SDWebUICreateState.asStateFlow()

    init {
        viewModelScope.launch {
            SDWebUICreateInentChannel.consumeAsFlow().collect{
                when(it){
                    is SDWebUICreateIntent.Text2Image -> {
                        text2image(
                            positionPrompt = it.positionPrompt,
                            negation = it.negationPrompt,
                            sessionHash = it.sessionHash
                        )
                    }
                    is SDWebUICreateIntent.DeepBooruReversePrompt -> {
                        deepBooruReversePrompt(
                            imageBase64 = it.imageBase64,
                            sessionHash = it.sessionHash
                        )
                    }
                    is SDWebUICreateIntent.ClipReversePrompt -> {
                        clipReversePrompt(
                            imageBase64 = it.imageBase64,
                            sessionHash = it.sessionHash
                        )
                    }
                    is SDWebUICreateIntent.Image2Image -> {
                        image2image(
                            positionPrompt = it.positionPrompt,
                            negationPrompt = it.negationPrompt,
                            imageBase64 = it.imageBase64,
                            sessionHash = it.sessionHash
                        )
                    }
                    is SDWebUICreateIntent.GetImageInformation -> {
                        getImageformation(
                            imageBase64 = it.imageBase64,
                            sessionHash = it.sessionHash
                        )
                    }
                }
            }
        }
    }

    fun post(intent: SDWebUICreateIntent){
        viewModelScope.launch {
            SDWebUICreateInentChannel.send(intent)
        }
    }

    private fun getImageformation(
        imageBase64: String,
        sessionHash: String
    ){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateError(exception))
            }
        }){
            SDWebUIRepo.getImageInformation(imageBase64 = imageBase64 , sessionHash =  sessionHash)
                .onStart {
                    withContext(Dispatchers.Main){
                        _SDWebUICreateState.emit(SDWebUICreateState.Creating)
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect{
                    it.data.takeIf {
                        it.isNotEmpty() && it.size == 3
                    }?.get(1)?.apply {
                        if(this is String){
                            withContext(Dispatchers.Main){
                                _SDWebUICreateState.emit(handleImageInformation(this@apply))
                            }
                        }
                    }
                }
        }
    }

    private fun clipReversePrompt(
        imageBase64: String,
        sessionHash: String
    ){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateError(exception))
            }
        }){
            SDWebUIRepo.clipReversePrompt(
                imageBase64 = imageBase64,
                sessionHash = sessionHash
            )
                .flowOn(Dispatchers.IO)
                .onStart {
                withContext(Dispatchers.Main) {
                    _SDWebUICreateState.emit(SDWebUICreateState.Creating)
                }
            }
                .collect{
                    it.data.takeIf {
                        it.isNotEmpty()
                    }?.get(0)?.apply {
                        if(this is String){
                            withContext(Dispatchers.Main){
                                _SDWebUICreateState.emit(SDWebUICreateState.ReversePromptSuccess(this@apply))
                            }
                        }
                    }
                }
        }
    }

    private fun deepBooruReversePrompt(
        imageBase64: String,
        sessionHash: String
    ){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateError(exception))
            }
        }){
            SDWebUIRepo.deepBooruReversePrompt(
                imageBase64 = imageBase64,
                sessionHash = sessionHash
            ).flowOn(Dispatchers.IO)
                .onStart {
                withContext(Dispatchers.Main) {
                    _SDWebUICreateState.emit(SDWebUICreateState.Creating)
                }
            }
                .collect{
                    it.data.takeIf {
                        it.isNotEmpty()
                    }?.get(0)?.apply {
                        if(this is String){
                            withContext(Dispatchers.Main){
                                _SDWebUICreateState.emit(SDWebUICreateState.ReversePromptSuccess(this@apply))
                            }
                        }
                    }
                }
        }
    }

    private fun text2image(positionPrompt: String , negation: String , sessionHash: String){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateError(exception))
            }
        }){
            SDWebUIRepo.text2image(
                positionPrompt = positionPrompt,
                negationPrompt = negation,
                sessionHash = sessionHash
            )
                .flowOn(Dispatchers.IO)
                .onStart {
                    withContext(Dispatchers.Main) {
                        _SDWebUICreateState.emit(SDWebUICreateState.Creating)
                    }
                }
                .collect{
                    handleResultBean(resultBean = it)?.let {
                        withContext(Dispatchers.Main){
                            _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateResult(
                                ApiUrl.resultUrl + it.name
                            )
                            )
                        }
                    }
                }
        }

    }

    private fun image2image(positionPrompt: String , negationPrompt: String , imageBase64: String , sessionHash: String){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateError(exception))
            }
        }){
            SDWebUIRepo.image2image(
                positionPrompt = positionPrompt,
                negationPrompt = negationPrompt,
                imageBase64 = imageBase64,
                sessionHash = sessionHash
            )
                .flowOn(Dispatchers.IO)
                .onStart {
                    withContext(Dispatchers.Main) {
                        _SDWebUICreateState.emit(SDWebUICreateState.Creating)
                    }
                }
                .collect{
                    handleResultBean(resultBean = it)?.let {
                        withContext(Dispatchers.Main){
                            _SDWebUICreateState.emit(SDWebUICreateState.ImageCreateResult(
                                ApiUrl.resultUrl + it.name
                            )
                            )
                        }
                    }
                }
        }

    }


    // 文生图,图生图结果解析
    private fun handleResultBean(resultBean: ResultBean): OutPutBean?{
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

    // 图片信息结果字符串解析
    private fun handleImageInformation(imageInformation:String): SDWebUICreateState.ImageInformation{
        val positionPromptLength = imageInformation.indexOf(ConstantPool.negation_prompt_heading)
        var positionPrompt = imageInformation.substring(0 , positionPromptLength )
        if(positionPrompt.endsWith("\n")) {
            positionPrompt = imageInformation.substring(0, positionPromptLength - 1)
        }
        val negationPromptLength = imageInformation.indexOf(ConstantPool.steps_heading)
        var negationPrompt = imageInformation.substring(positionPromptLength + ConstantPool.negation_prompt_heading.length, negationPromptLength)
        if(negationPrompt.endsWith("\n")) {
            negationPrompt = imageInformation.substring(positionPromptLength + ConstantPool.negation_prompt_heading.length, negationPromptLength - 1)
        }
        return SDWebUICreateState.ImageInformation(
            positionPrompt = positionPrompt,
            negationPrompt = negationPrompt
        )
    }

}