package com.example.aidraw

import android.app.Application
import android.content.Context
import com.example.aidraw.room.UserDataBase
import com.tencent.mmkv.MMKV

class MyApplication: Application() {

    companion object{
        private lateinit var instance: MyApplication
        lateinit var userDataBase: UserDataBase

        // 全局上下文
        val applicationContext: Context
            get() = instance
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this)
        userDataBase = UserDataBase.getDatabase(this)
    }
}