package com.example.aidraw

import android.app.Application
import android.content.Context

class MyApplication: Application() {

    companion object{
        private lateinit var instance: MyApplication

        // 全局上下文
        val applicationContext: Context
            get() = instance
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}