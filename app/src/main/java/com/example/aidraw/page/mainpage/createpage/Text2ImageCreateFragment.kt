package com.example.aidraw.page.mainpage.createpage

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import com.example.aidraw.databinding.FragmentText2ImageCreateBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.CreateImageViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.launch


class Text2ImageCreateFragment(val text2ImageIntent: CreateImageIntent.Text2Image) : Fragment() {

    private lateinit var fragmengText2ImageCreateBinding: FragmentText2ImageCreateBinding
    private lateinit var text2ImageLoadingLayout: LinearLayoutCompat
    private lateinit var text2ImageLoadingIcon: ImageView
    private lateinit var text2ImageResult: ImageView
    private lateinit var loadingAnimator: ObjectAnimator
    private val sdWebUICreateViewModel:SDWebUICreateViewModel by activityViewModels()
    private val createImageViewModel: CreateImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmengText2ImageCreateBinding = FragmentText2ImageCreateBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmengText2ImageCreateBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initData()
        initWidget()
        handleState()
        sdWebUICreateViewModel.post(
            SDWebUICreateIntent.Text2Image(
                positionPrompt = text2ImageIntent.position,
                negationPrompt = text2ImageIntent.negation,
                seesionHash = ExUtil.getAndroidId(requireContext())
            )
        )

    }

    private fun initData(){
        text2ImageLoadingLayout = fragmengText2ImageCreateBinding.text2ImageLoadingLayout
        text2ImageLoadingIcon = fragmengText2ImageCreateBinding.text2ImageLoadingIcon
        text2ImageResult = fragmengText2ImageCreateBinding.text2ImageResult

        loadingAnimator = ObjectAnimator.ofFloat(
            text2ImageLoadingIcon,
            "rotation",
            360f , 0f
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            duration = 3000
        }
    }

    private fun initWidget(){
    }

    private fun handleState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                it?.let {
                    if(it is SDWebUICreateState.Creating){
                        loadingAnimator.start()
                    }else if(it is SDWebUICreateState.ImageCreateResult){
                        Glide.with(requireContext())
                            .asBitmap()
                            .load(it.name)
                            .into(object : ImageViewTarget<Bitmap>(text2ImageResult){
                                override fun setResource(resource: Bitmap?) {
                                    loadingAnimator.cancel()
                                    text2ImageLoadingLayout.visibility = View.GONE
                                    resource?.let {
                                        text2ImageResult.setImageBitmap(resource)
                                        createImageViewModel.setCurentCreateResult(resultBitmap = resource)
                                    }
                                }

                            })
                    }else if(it is SDWebUICreateState.ImageCreateError){

                    }
                }
            }
        }
    }
}