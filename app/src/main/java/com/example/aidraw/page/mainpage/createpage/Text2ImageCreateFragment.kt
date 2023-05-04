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
import com.example.aidraw.databinding.FragmentText2ImageCreateBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.OtherIntent
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.state.OtherState
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.OtherViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.launch


class Text2ImageCreateFragment(val text2ImageIntent: CreateImageIntent.Text2Image) : Fragment() {

    private lateinit var fragmengText2ImageCreateBinding: FragmentText2ImageCreateBinding
    private lateinit var text2ImageLoadingLayout: LinearLayoutCompat
    private lateinit var text2ImageLoadingIcon: ImageView
    private lateinit var text2ImageResult: ImageView
    private lateinit var loadingAnimator: ObjectAnimator
    private val otherViewModel: OtherViewModel by activityViewModels()
    private val sdWebUICreateViewModel:SDWebUICreateViewModel by activityViewModels()

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
                sessionHash = ExUtil.getAndroidId(requireContext())
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
            otherViewModel.otherState.collect{
                if(it is OtherState.RefreshImageStart){
                    text2ImageLoadingLayout.visibility = View.VISIBLE
                    loadingAnimator.start()
                    text2ImageResult.visibility = View.GONE
                    text2ImageResult.setImageDrawable(null)
                    sdWebUICreateViewModel.post(
                        SDWebUICreateIntent.Text2Image(
                            positionPrompt = text2ImageIntent.position,
                            negationPrompt =  text2ImageIntent.negation,
                            sessionHash = ExUtil.getAndroidId(requireContext())
                        )
                    )
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
                            .into(object : ImageViewTarget<Drawable>(text2ImageResult){
                                override fun setResource(resource: Drawable?) {
                                    text2ImageResult.setImageDrawable(resource)
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    super.onResourceReady(resource, transition)
                                    text2ImageResult.visibility = View.VISIBLE
                                    loadingAnimator.cancel()
                                    text2ImageLoadingLayout.visibility = View.GONE
//                                    text2ImageResult.setImageDrawable(resource)
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