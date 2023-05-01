package com.example.aidraw.page.mainpage.createpage

import android.animation.ObjectAnimator
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
import com.bumptech.glide.request.transition.Transition
import com.example.aidraw.databinding.FragmentImage2ImageCreateBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.OtherIntent
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.state.OtherState
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.util.ImageUtil
import com.example.aidraw.viewmodel.OtherViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.launch
import java.io.File


class Image2ImageCreateFragment(val image2ImageIntent: CreateImageIntent.Image2Image) : Fragment() {

    private lateinit var fragmentImage2ImageCreateBinding: FragmentImage2ImageCreateBinding
    private lateinit var image2ImageLoadingLayout: LinearLayoutCompat
    private lateinit var image2ImageLoadingIcon: ImageView
    private lateinit var image2ImageResult: ImageView
    private lateinit var loadingAnimator: ObjectAnimator
    private val sdWebUICreateViewModel:SDWebUICreateViewModel by activityViewModels()
    private val otherViewModel:OtherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentImage2ImageCreateBinding = FragmentImage2ImageCreateBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentImage2ImageCreateBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        handleState()
    }

    private fun init(){
        initData()
        initWidget()
        val file = File(image2ImageIntent.path)
        if(file.exists()){
            ImageUtil.imageToBase64(path = image2ImageIntent.path)?.apply {
                sdWebUICreateViewModel.post(
                    SDWebUICreateIntent.Image2Image(
                        positionPrompt = image2ImageIntent.position,
                        negationPrompt = image2ImageIntent.negation,
                        imageBase64 = this ,
                        sessionHash = ExUtil.getAndroidId(requireContext())
                    )
                )
            }
        }
    }

    private fun initData(){
        image2ImageLoadingLayout = fragmentImage2ImageCreateBinding.image2ImageLoadingLayout
        image2ImageLoadingIcon = fragmentImage2ImageCreateBinding.image2ImageLoadingIcon
        image2ImageResult = fragmentImage2ImageCreateBinding.image2ImageResult

        loadingAnimator = ObjectAnimator.ofFloat(
            image2ImageLoadingIcon,
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
            otherViewModel.otherState.collect{
                if(it is OtherState.RefreshImageStart){
                    image2ImageLoadingLayout.visibility = View.VISIBLE
                    loadingAnimator.start()
                    image2ImageResult.visibility = View.GONE
                    image2ImageResult.setImageDrawable(null)
                    val file = File(image2ImageIntent.path)
                    if(file.exists()){
                        ImageUtil.imageToBase64(path = image2ImageIntent.path)?.apply {
                            sdWebUICreateViewModel.post(
                                SDWebUICreateIntent.Image2Image(
                                    positionPrompt = image2ImageIntent.position,
                                    negationPrompt = image2ImageIntent.negation,
                                    imageBase64 = this ,
                                    sessionHash = ExUtil.getAndroidId(requireContext())
                                )
                            )
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                it?.let {
                    if(it is SDWebUICreateState.Creating){
                        loadingAnimator.start()
                    }else if(it is SDWebUICreateState.ImageCreateResult){
                        Glide.with(requireContext())
                            .load(it.name)
                            .into(object : ImageViewTarget<Drawable>(image2ImageResult){
                                override fun setResource(resource: Drawable?) {
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    super.onResourceReady(resource, transition)
                                    image2ImageResult.visibility = View.VISIBLE
                                    loadingAnimator.cancel()
                                    image2ImageLoadingLayout.visibility = View.GONE
                                    image2ImageResult.setImageDrawable(resource)
                                    otherViewModel.post(OtherIntent.refreshImageEnd(it.name))
                                }
                            })
                    }else if(it is SDWebUICreateState.ImageCreateError){

                    }
                }
            }
        }
    }


}