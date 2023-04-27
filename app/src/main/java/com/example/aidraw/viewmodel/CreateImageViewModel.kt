package com.example.aidraw.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateImageViewModel: ViewModel() {

    private val _currentCreateResult : MutableLiveData<Bitmap> = MutableLiveData()
    val currentCreateResult: LiveData<Bitmap> = _currentCreateResult

    fun getCurrentCreateResult() = _currentCreateResult.value
    fun setCurentCreateResult(resultBitmap: Bitmap){
        _currentCreateResult.postValue(resultBitmap)
    }
}