package com.example.aidraw.page.mainpage.createpage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.aidraw.R
import com.example.aidraw.databinding.ActivityCreateImageBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.OtherIntent
import com.example.aidraw.page.mainpage.othepage.LoadingDialog
import com.example.aidraw.pool.SpKey
import com.example.aidraw.state.OtherState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.CreateImageViewModel
import com.example.aidraw.viewmodel.OtherViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateImageActivity : AppCompatActivity() {

    private val createImageViewModel: CreateImageViewModel by viewModels()
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private val otherViewModel: OtherViewModel by viewModels()
    private lateinit var activityCreateImageBinding: ActivityCreateImageBinding
    private lateinit var downloadButton: Button
    private lateinit var shareButton: ImageButton
    private lateinit var refreshButton: ImageButton
    private lateinit var returnButton: ImageView
    private lateinit var createImageTitle: TextView
    private lateinit var createImageLayout: ConstraintLayout
    private lateinit var launcher: ActivityResultLauncher<Array<String>>
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCreateImageBinding = ActivityCreateImageBinding.inflate(layoutInflater)
        ExUtil.transparentStatusBar(this)
        val layoutParams = activityCreateImageBinding.createImageTitleLayout.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(0 , ExUtil.getStatusBarHeight(this) , 0 , 0)
        setContentView(activityCreateImageBinding.root)
        init()
    }

    private fun init(){
        initData()
        initWidget()
        handleState()
        handleIntent()
    }

    private fun initData(){
        downloadButton = activityCreateImageBinding.downloadButton
        shareButton = activityCreateImageBinding.shareButton
        refreshButton = activityCreateImageBinding.refreshButton
        createImageTitle = activityCreateImageBinding.createImageTitle
        returnButton = activityCreateImageBinding.createImageReturnIcon
        createImageLayout = activityCreateImageBinding.createImageLayout



    }

    private fun initWidget(){

        downloadButton.isEnabled = false
        shareButton.isEnabled = false
        refreshButton.isEnabled = false

        returnButton.setOnClickListener {
            this.finish()
        }
        downloadButton.setOnClickListener {
            createImageViewModel.getCurrentCreateResult()?.let {
                otherViewModel.post(OtherIntent.downloadImage(it))
            }
            if(createImageViewModel.getCurrentCreateResult() == null){
                ExUtil.toast(this,R.string.download_tip)
            }
        }
        shareButton.setOnClickListener {
            if(createImageViewModel.getCurrentCreateResult() == null){
                ExUtil.toast(this , R.string.share_tip)
            }else{
                createImageViewModel.getCurrentImageUri()?.let {
                    ExUtil.sharePhoto(this , it , this.contentResolver)
                }
                if(createImageViewModel.getCurrentImageUri() == null){
                    createImageViewModel.getCurrentCreateResult()?.let {
                        otherViewModel.post(OtherIntent.shareImage(it))
                    }
                }
            }
        }
        refreshButton.setOnClickListener {
            otherViewModel.post(OtherIntent.refreshImageStart)
        }
    }


    private fun handleIntent(){
        intent?.apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                val text2ImageIntent = this.getParcelableExtra(
                    SpKey.text_2_image ,
                    CreateImageIntent.Text2Image::class.java
                )
                val image2ImageIntent = this.getParcelableExtra(
                    SpKey.image_2_image ,
                    CreateImageIntent.Image2Image::class.java
                )
                if(text2ImageIntent != null){
                    val text2ImageCreateFragment = Text2ImageCreateFragment(text2ImageIntent)
                    supportFragmentManager.beginTransaction()
                        .add(createImageLayout.id , text2ImageCreateFragment)
                        .commit()
                    setTitle(getString(R.string.text_2_image_title))
                }else if(image2ImageIntent != null){
                    val image2ImageCreateFragment = Image2ImageCreateFragment(image2ImageIntent)
                    supportFragmentManager.beginTransaction()
                        .add(createImageLayout.id , image2ImageCreateFragment)
                        .commit()
                    setTitle(getString(R.string.image_2_image_title))
                }
            }else{
                val text2ImageIntent = this.getParcelableExtra<CreateImageIntent.Text2Image>(
                    SpKey.text_2_image
                )
                val image2ImageIntent = this.getParcelableExtra<CreateImageIntent.Image2Image>(
                    SpKey.image_2_image
                )
                if(text2ImageIntent != null){
                    val text2ImageCreateFragment = Text2ImageCreateFragment(text2ImageIntent)
                    supportFragmentManager.beginTransaction()
                        .add(createImageLayout.id , text2ImageCreateFragment)
                        .commit()
                    setTitle(getString(R.string.text_2_image_title))
                }else if(image2ImageIntent != null){
                    val image2ImageCreateFragment = Image2ImageCreateFragment(image2ImageIntent)
                    supportFragmentManager.beginTransaction()
                        .add(createImageLayout.id , image2ImageCreateFragment)
                        .commit()
                    setTitle(getString(R.string.image_2_image_title))
                }
            }

        }
    }

    private fun handleState(){
        lifecycleScope.launch {
            otherViewModel.otherState.collect {
                it?.apply {
                    when (this) {
                        is OtherState.downloadImageSuccess -> {
                            createImageViewModel.setCurrentImageUri(this.imageUri)
                            loadingDialog.dismissAllowingStateLoss()
                            ExUtil.toast(
                                this@CreateImageActivity,
                                R.string.save_success
                            )
                        }

                        is OtherState.shareImageSuccess -> {
                            createImageViewModel.setCurrentImageUri(this.imageUri)
                            loadingDialog.dismissAllowingStateLoss()
                            ExUtil.sharePhoto(
                                this@CreateImageActivity,
                                this.imageUri,
                                this@CreateImageActivity.contentResolver
                            )
                        }

                        is OtherState.downloadError -> {
                            loadingDialog.dismissAllowingStateLoss()
                            ExUtil.toast(
                                this@CreateImageActivity,
                                R.string.network_error
                            )
                        }

                        is OtherState.downloading -> {
                            loadingDialog.show(
                                supportFragmentManager,
                                LoadingDialog.TAG
                            )
                        }

                        is OtherState.refreshImageStart -> {
                            createImageViewModel.setCurentCreateResult(null)
                            createImageViewModel.setCurrentImageUri(null)
                            downloadButton.isEnabled = false
                            shareButton.isEnabled = false
                            refreshButton.isEnabled = false
                        }
                        is OtherState.refreshImageEnd -> {
                            createImageViewModel.setCurentCreateResult(this.imageUrl)
                            shareButton.isEnabled = true
                            downloadButton.isEnabled = true
                            refreshButton.isEnabled = true
                        }
                    }
                }
            }

        }
    }

    private fun setTitle(title: String){
        createImageTitle.text = "${title} "
        ExUtil.setLinearGradientText(createImageTitle , this)
    }
}