package com.example.aidraw.util

import android.content.Context
import android.provider.Settings
import com.example.aidraw.pool.ConstantPool

object ExUtil {
    fun getAndroidId(context: Context) :String{

        val sharedPreferences = context.getSharedPreferences(
            "AndroidId",
            Context.MODE_PRIVATE
        )
        var sessionHash = sharedPreferences.getString(ConstantPool.SessionHash , "")
        // 不存在或者长度为0时
        if(sessionHash == null || sessionHash.isEmpty()){
            sessionHash = Settings.System.getString(context.contentResolver , Settings.Secure.ANDROID_ID)
            sharedPreferences.edit().putString(
                ConstantPool.SessionHash,
                sessionHash
            ).apply()
            return sessionHash
        }else{
            return sessionHash
        }
    }
}