package com.example.aidraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _currentPageIndex = MutableLiveData<Int>()
    val currentPageIndex : LiveData<Int> = _currentPageIndex

    fun getCurrentPageIndex() = _currentPageIndex.value

    fun setCurrentPageIndex(index : Int){
        _currentPageIndex.postValue(index)
    }
}