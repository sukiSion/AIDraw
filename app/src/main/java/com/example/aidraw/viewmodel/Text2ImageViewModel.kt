package com.example.aidraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Text2ImageViewModel: ViewModel() {
    private val _currentPosition: MutableLiveData<String> = MutableLiveData()
    val currentPosition: LiveData<String> = _currentPosition

    private val _currentNegation: MutableLiveData<String> = MutableLiveData()
    val currentNegation: LiveData<String> = _currentNegation

    private val _uploadImagePath: MutableLiveData<String> = MutableLiveData()
    val uploadImagePath: LiveData<String> = _uploadImagePath

    fun setUploadImagePath(path: String){
        _uploadImagePath.postValue(path)
    }

    fun getUploadImagePath() = _uploadImagePath.value

    fun setPosition(position: String){
        _currentPosition.postValue(position)
    }

    fun getPosition() = _currentPosition.value

    fun setNegation(negation: String){
        _currentNegation.postValue(negation)
    }

    fun getNegation() = _currentNegation.value



}