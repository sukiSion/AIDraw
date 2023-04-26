package com.example.aidraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 用于存放AI绘画配置数据
class SettingViewModel: ViewModel() {

    private val _faceRepairSelected = MutableLiveData<Boolean>(false)
    val faceRepairSelected:LiveData<Boolean> = _faceRepairSelected


    private val _hdRepairSelected = MutableLiveData<Boolean>(false)
    val hdRepairSelected:LiveData<Boolean> = _hdRepairSelected

    private val _currentWidth = MutableLiveData<Int>()
    val currentWidth:LiveData<Int> = _currentWidth

    private val _currentHeight = MutableLiveData<Int>()
    val currentHeight:LiveData<Int> = _currentHeight

    private val _currentSteps = MutableLiveData<Int>()
    val currentSteps:LiveData<Int> = _currentSteps

    private val _currentScale = MutableLiveData<Float>()
    val currentScale:LiveData<Float> = _currentScale

    private val _currentDenoising = MutableLiveData<Float>()
    val currentDenoising:LiveData<Float> = _currentDenoising

    private val _currentSampler = MutableLiveData<String>()
    val currentSampler:LiveData<String> = _currentSampler



    fun changeFaceRepairState(){
        if(_faceRepairSelected.value == true){
            _faceRepairSelected.postValue(false)

        }else{
            _faceRepairSelected.postValue(true)
        }
    }

    fun changeHdRepairState(){
        if(_hdRepairSelected.value == true){
            _hdRepairSelected.postValue(false)

        }else{
            _hdRepairSelected.postValue(true)
        }
    }

    fun changeWidth(value: Int){
        _currentWidth.postValue(value)
    }

    fun changeHeight(value: Int){
        _currentHeight.postValue(value)
    }

    fun changeSteps(value: Int){
        _currentSteps.postValue(value)
    }

    fun changeScale(value: Float){
        _currentScale.postValue(value)
    }

    fun changeSampler(value: String){
        _currentSampler.postValue(value)
    }

    fun changDenoising(value: Float){
        _currentDenoising.postValue(value)
    }

}