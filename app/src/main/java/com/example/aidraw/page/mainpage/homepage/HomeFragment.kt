package com.example.aidraw.page.mainpage.homepage

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentHomeBinding
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.launch

class HomeFragment:  Fragment() {

    private val sdWebUICreateViewModel: SDWebUICreateViewModel by activityViewModels()
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var createButton: Button
    private lateinit var positionPromptLayout: ConstraintLayout
    private lateinit var negationPromptLayout: ConstraintLayout
    private lateinit var positionPromptInput: EditText
    private lateinit var negationPromptInput: EditText
    private lateinit var inputOnFourceBackground: GradientDrawable


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSDWebUIState()
        init()
    }

    private fun init(){
        initData()
        initWidget()
    }

    private fun initWidget() {

        positionPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    positionPromptLayout.background  = inputOnFourceBackground
                }
                false ->  {
                    positionPromptLayout.background  = null
                }
            }
        }

        negationPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    negationPromptLayout.background  = inputOnFourceBackground
                }
                false ->  {
                    negationPromptLayout.background  = null
                }
            }
        }

        createButton.setOnClickListener {
            negationPromptInput.clearFocus()
            positionPromptInput.clearFocus()
            val position = positionPromptInput.editableText.toString()
            val negation = negationPromptInput.editableText.toString()
            if(position.isNotBlank() && negation.isNotBlank()){
                sdWebUICreateViewModel.post(
                    SDWebUICreateIntent.Text2Image(
                        positionPrompt = position,
                        negationPrompt = negation,
                        seesionHash = ExUtil.getAndroidId(requireContext())
                    )
                )
            }else{
                if(position.isBlank()){
                    ExUtil.toast(requireContext() , R.string.input_position_tip)
                }else if(negation.isBlank()){
                    ExUtil.toast(requireContext() , R.string.input_negation_tip)
                }
            }
        }
    }

    private fun initData() {
        positionPromptLayout = fragmentHomeBinding.positionPromptLayout
        negationPromptLayout = fragmentHomeBinding.negationPromptLayout
        positionPromptInput = fragmentHomeBinding.positionPromptInput
        negationPromptInput =  fragmentHomeBinding.negationPromptInput
        createButton = fragmentHomeBinding.createButton

        inputOnFourceBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(this@HomeFragment.requireContext() , R.color.green_ffd6),
                ContextCompat.getColor(this@HomeFragment.requireContext() , R.color.blue_87ff),
                ContextCompat.getColor(this@HomeFragment.requireContext() , R.color.purple_55ff)
            )
        ).apply {
            this.cornerRadius =  ExUtil.dip2px(this@HomeFragment.requireContext() , 10f)
        }
        positionPromptInput.text = Editable.Factory.getInstance().newEditable("(masterpiece,best quality:1.6), (solo,1girl,bust:1.2), scenery,(Mountain, maid), flower,butterfly")
        negationPromptInput.text = Editable.Factory.getInstance().newEditable("nsfw,(worst quality,low quality,normal quality:1.6),a realistic face, impossible anatomy,impossible arms and legs, impossible hands and feet, impossible fingers and toes, impossible numbers of limbs and fingers, impossible clothes and shoes, watermark ")
    }

    private fun handleSDWebUIState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                it?.let {
                    when(it){
                        is SDWebUICreateState.Text2ImageResult -> {
                            Glide.with(requireContext())
                                .load(it.name)
                                .into(fragmentHomeBinding.outOutFile)
                        }
                        is SDWebUICreateState.Text2ImageError -> {

                        }
                    }
                }
            }
        }
    }
}