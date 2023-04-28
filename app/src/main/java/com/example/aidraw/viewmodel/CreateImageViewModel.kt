package com.example.aidraw.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateImageViewModel: ViewModel() {

    // 当前图片的链接，通过下载时通过链接下载确保图片的完整
    private val _currentCreateResult : MutableLiveData<String?> = MutableLiveData()

    private val _currentImageUri : MutableLiveData<Uri?> = MutableLiveData()


    fun getCurrentImageUri() = _currentImageUri.value
    fun setCurrentImageUri(imageUri: Uri?){
        _currentImageUri.postValue(imageUri)
    }

    fun getCurrentCreateResult() = _currentCreateResult.value
    fun setCurentCreateResult(resultUrl: String?){
        _currentCreateResult.postValue(resultUrl)
    }
}