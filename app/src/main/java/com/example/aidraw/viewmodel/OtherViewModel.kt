package com.example.aidraw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidraw.intent.OtherIntent
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.repo.OtherRepo
import com.example.aidraw.state.OtherState
import com.example.aidraw.util.SavePhotoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtherViewModel: ViewModel() {

    private var OtherIntentChannel: Channel<OtherIntent> = Channel(Channel.UNLIMITED)

    private val _otherState: MutableStateFlow<OtherState?> = MutableStateFlow(null)
    val otherState : StateFlow<OtherState?> = _otherState.asStateFlow()



    init {
        viewModelScope.launch {
            OtherIntentChannel.consumeAsFlow().collect{
                when(it){
                    is OtherIntent.shareImage -> {
                        shareImage(it.imageUrl)
                    }
                    is OtherIntent.downloadImage -> {
                        dowmloadImage(it.imageUrl)
                    }
                    is OtherIntent.refreshImageStart -> {
                        refreshImageStart()
                    }
                    is OtherIntent.refreshImageEnd -> {
                        refreshImageEnd(it.imageUrl)
                    }
                }
            }
        }
    }

    fun post(intent: OtherIntent){
        viewModelScope.launch {
            OtherIntentChannel.send(intent)
        }
    }

    private fun refreshImageStart(){
        viewModelScope.launch(context = viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context, exception ->
            viewModelScope.launch(context + Dispatchers.Main) {
                _otherState.emit(OtherState.DownloadError(exception))
            }
        }){
            flow {
              emit(OtherState.RefreshImageStart)
            }
                .flowOn(Dispatchers.IO)
                .collect{
                    _otherState.emit(it)
            }
        }
    }

    private fun refreshImageEnd(imageUrl: String){
        viewModelScope.launch(context = viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context, exception ->
            viewModelScope.launch(context + Dispatchers.Main) {
                _otherState.emit(OtherState.DownloadError(exception))
            }
        }){
            flow {
                emit(imageUrl)
            }
                .flowOn(Dispatchers.IO)
                .collect{
                    _otherState.emit(OtherState.RefreshImageEnd(imageUrl))
                }
        }
    }

    private fun dowmloadImage(imageUrl: String){
        viewModelScope.launch(context = viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
            context, exception ->
            viewModelScope.launch(context + Dispatchers.Main) {
                _otherState.emit(OtherState.DownloadError(exception))
            }
        }){
            OtherRepo.downlaodImage(imageUrl)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _otherState.emit(OtherState.Downloading)
                }
                .collect{
                    withContext(Dispatchers.IO){
                        it.byteStream().use {
                            SavePhotoUtil.saveToAlbumWithNet(
                                it,
                                fileName = "${System.currentTimeMillis()}${ConstantPool.image_suffix}"
                            ) {
                                viewModelScope.launch(Dispatchers.Main) {
                                    _otherState.emit(OtherState.DownloadImageSuccess(it))
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun shareImage(imageUrl: String){
        viewModelScope.launch(context = viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context, exception ->
            viewModelScope.launch(context + Dispatchers.Main) {
                _otherState.emit(OtherState.DownloadError(exception))
            }
        }){
            OtherRepo.downlaodImage(imageUrl)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _otherState.emit(OtherState.Downloading)
                }
                .collect{
                    withContext(Dispatchers.IO){
                        it.byteStream().use {
                            SavePhotoUtil.saveToAlbumWithNet(
                                it,
                                fileName = "${System.currentTimeMillis()}${ConstantPool.image_suffix}"
                            ) {
                                viewModelScope.launch(Dispatchers.Main) {
                                    _otherState.emit(OtherState.ShareImageSuccess(it))
                                }
                            }
                        }
                    }
                }
        }
    }

}