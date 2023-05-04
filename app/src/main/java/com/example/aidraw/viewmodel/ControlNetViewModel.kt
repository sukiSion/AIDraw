package com.example.aidraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlNetViewModel: ViewModel() {

    private val _cannySelected = MutableLiveData(false)
    val cannySelected: LiveData<Boolean> = _cannySelected

    private val _segSelected = MutableLiveData(false)
    val segSelected: LiveData<Boolean> = _segSelected

    private val _leResSelected = MutableLiveData(false)
    val leResSelected: LiveData<Boolean> = _leResSelected

    private val _controlNetImagePath: MutableLiveData<String> = MutableLiveData()
    val controlNetImagePath: LiveData<String> = _controlNetImagePath

    fun setContrlNetImagePath(imagePath : String){
        _controlNetImagePath.postValue(imagePath)
    }

    fun changeCannyState(){
        if(_cannySelected.value == true){
            _cannySelected.postValue(false)

        }else{
            _cannySelected.postValue(true)
        }
    }

    fun changeSegState(){
        if(_segSelected.value == true){
            _segSelected.postValue(false)

        }else{
            _segSelected.postValue(true)
        }
    }

    fun changeLeResState(){
        if(_leResSelected.value == true){
            _leResSelected.postValue(false)

        }else{
            _leResSelected.postValue(true)
        }
    }

    fun getCanny() = _cannySelected.value

    fun getSeg() = _segSelected.value

    fun getLeRes() = _leResSelected.value

    fun getControlImagePath() = _controlNetImagePath.value
}