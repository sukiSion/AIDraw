package com.example.aidraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Image2ImageViewModel: ViewModel() {
    private val  _currentImagePath: MutableLiveData<String> = MutableLiveData()
    val currentImagePath:LiveData<String> = _currentImagePath

    fun getCurrentImagePath() = currentImagePath.value
    fun setCurrrentImagePath(path: String){
        _currentImagePath.postValue(path)
    }
}