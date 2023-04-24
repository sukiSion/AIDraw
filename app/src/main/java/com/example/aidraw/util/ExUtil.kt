package com.example.aidraw.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.aidraw.pool.ConstantPool

object ExUtil {
    // 获取设备id
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

    // 获取状态栏高度
    fun getStatusBarHeight(acticity: Activity): Int {
        val window = acticity.window
        val localRect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(localRect)
        var mStatusBarHeight = localRect.top
        if (0 == mStatusBarHeight) {
            try {
                val localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 =
                    localClass.getField("status_bar_height")[localObject].toString().toInt()
                mStatusBarHeight = acticity.resources.getDimensionPixelSize(i5)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        if (0 == mStatusBarHeight) {
            val resourceId: Int =
                acticity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                mStatusBarHeight = acticity.resources.getDimensionPixelSize(resourceId)
            }
        }
        return mStatusBarHeight
    }

    // dip转px
    fun dip2px(context: Context, dipValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f)
    }

    // 透明状态栏
    fun transparentStatusBar(activity: Activity) {
        transparentStatusBar(activity.window)
    }

    // 透明状态栏
    fun transparentStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val vis = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or vis
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

}