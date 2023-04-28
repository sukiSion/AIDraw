package com.example.aidraw.page.mainpage.othepage

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aidraw.databinding.ActivityLaunchBinding
import com.example.aidraw.page.mainpage.MainActivity
import com.example.aidraw.util.ExUtil

class LaunchActivity : AppCompatActivity() {

    private lateinit var activityLaunchBinding: ActivityLaunchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLaunchBinding = ActivityLaunchBinding.inflate(layoutInflater)
        ExUtil.transparentStatusBar(this)
        setContentView(activityLaunchBinding.root)
        activityLaunchBinding.launchButton.setOnClickListener {
            val loginBottomDialogSheet = LoginBottomDialogSheet()
            loginBottomDialogSheet.show(
                supportFragmentManager,
                LoginBottomDialogSheet.TAG
            )
        }

    }
}