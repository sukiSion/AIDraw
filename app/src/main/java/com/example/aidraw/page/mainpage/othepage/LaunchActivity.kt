package com.example.aidraw.page.mainpage.othepage

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.aidraw.R
import com.example.aidraw.databinding.ActivityLaunchBinding
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.page.mainpage.MainActivity
import com.example.aidraw.pool.CachePool
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.state.UserState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import com.example.aidraw.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class LaunchActivity : AppCompatActivity() {

    private lateinit var activityLaunchBinding: ActivityLaunchBinding
    private val userViewModel: UserViewModel by viewModels()
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private val fullScreenLoadingDialog: FullScreenLoadingDialog by lazy {
        FullScreenLoadingDialog()
    }

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

        handleState()
    }

    private fun handleState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                if(it is SDWebUICreateState.Creating){
                    fullScreenLoadingDialog.show(
                        this@LaunchActivity.supportFragmentManager,
                        FullScreenLoadingDialog.TAG
                    )
                }
                else if(it is SDWebUICreateState.ImageCreateError){
                    if(fullScreenLoadingDialog.isVisible){
                        fullScreenLoadingDialog.dismissAllowingStateLoss()
                    }
                }
                else if(it is SDWebUICreateState.InitAppSuccess){
                    CachePool.instance.supportModels = it.aboutModelData.choices
                    CachePool.instance.model = it.aboutModelData.value
                    val intent = Intent(this@LaunchActivity , MainActivity::class.java)
                    startActivity(intent)
                    this@LaunchActivity.finish()
                    ExUtil.toast(
                        this@LaunchActivity,
                        R.string.init_app_success
                    )
                }
            }
        }

        lifecycleScope.launch {
            userViewModel.userState.collect{
                if(it is UserState.addUserSuccess){
                    sdWebUICreateViewModel.post(
                        SDWebUICreateIntent.InitApp(
                            ExUtil.getAndroidId(this@LaunchActivity)
                        )
                    )
                }

                else if(it is UserState.queryUserSuccess){
                    sdWebUICreateViewModel.post(
                        SDWebUICreateIntent.InitApp(
                            ExUtil.getAndroidId(this@LaunchActivity)
                        )
                    )
                }
            }
        }
    }
}