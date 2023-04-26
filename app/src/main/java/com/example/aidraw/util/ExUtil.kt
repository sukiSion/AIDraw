package com.example.aidraw.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.example.aidraw.R
import com.example.aidraw.pool.ConstantPool
import java.io.File
import java.io.FileInputStream

object ExUtil {


    // 手机图片文件转base64
    fun encodeToBase64(file: File): String {
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(file.length().toInt())
        inputStream.read(buffer)
        val outputStream = java.io.ByteArrayOutputStream()
        val byteArray: ByteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    /**
     * 切换软键盘显示/隐藏状态
     */
    fun toggleKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    // 判断软键盘是否弹出的代码
    fun isSoftKeyboardVisible(activity: Activity): Boolean {
        val decorView = activity.window.decorView
        val visibleHeight = Rect().apply {
            decorView.getWindowVisibleDisplayFrame(this)
        }.height()

        val screenHeight = decorView.height

        // If the difference is greater than a threshold value, assume the keyboard is visible
        return (screenHeight - visibleHeight) > (screenHeight / 4)
    }

    // 土司
    fun toast(context: Context , @StringRes value: Int){
        Toast.makeText(
            context,
            value,
            Toast.LENGTH_SHORT
        ).show()
    }

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

    // 设置渐变色字体
    fun setLinearGradientText(
        textView: TextView,
        context: Context
    ){

        textView.viewTreeObserver.addOnDrawListener {
            val linearGradient = LinearGradient(
                0f ,
                0f ,
                textView.width.toFloat(),
                textView.height.toFloat(),
                intArrayOf(
                    ContextCompat.getColor(context , R.color.green_ffd6),
                    ContextCompat.getColor(context , R.color.blue_87ff),
                    ContextCompat.getColor(context , R.color.purple_55ff),

                    ),
                null,
                Shader.TileMode.CLAMP
            )
            textView.paint.setShader(linearGradient)
            textView.invalidate()
        }

    }

}