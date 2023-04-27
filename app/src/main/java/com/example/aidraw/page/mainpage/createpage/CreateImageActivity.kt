package com.example.aidraw.page.mainpage.createpage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.aidraw.R
import com.example.aidraw.databinding.ActivityCreateImageBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.page.mainpage.homepage.Image2ImageFragment
import com.example.aidraw.pool.SpKey
import com.example.aidraw.util.ExUtil
import com.example.aidraw.util.SavePhotoUtil
import com.example.aidraw.viewmodel.CreateImageViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel

class CreateImageActivity : AppCompatActivity() {

    private val createImageViewModel: CreateImageViewModel by viewModels()
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private lateinit var activityCreateImageBinding: ActivityCreateImageBinding
    private lateinit var downloadButton: Button
    private lateinit var shareButton: ImageButton
    private lateinit var returnButton: ImageView
    private lateinit var createImageTitle: TextView
    private lateinit var createImageLayout: ConstraintLayout

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
        handleIntent()
    }

    private fun initData(){
        downloadButton = activityCreateImageBinding.downloadButton
        shareButton = activityCreateImageBinding.shareButton
        createImageTitle = activityCreateImageBinding.createImageTitle
        returnButton = activityCreateImageBinding.createImageReturnIcon
        createImageLayout = activityCreateImageBinding.createImageLayout


        createImageViewModel.currentCreateResult.observe(
            this
        ){
            shareButton.isEnabled = true
            downloadButton.isEnabled = true
        }
    }

    private fun initWidget(){

        downloadButton.isEnabled = false
        shareButton.isEnabled = false

        returnButton.setOnClickListener {
            this.finish()
        }
        downloadButton.setOnClickListener {
            createImageViewModel.getCurrentCreateResult()?.let {
                SavePhotoUtil.saveToAlbum(
                    it,
                    this,
                    "${System.currentTimeMillis()}"
                ){
                    ExUtil.toast(
                        this,
                        R.string.save_success
                    )
                }
            }
        }
        shareButton.setOnClickListener {

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

    private fun setTitle(title: String){
        createImageTitle.text = "${title} "
        ExUtil.setLinearGradientText(createImageTitle , this)
    }
}