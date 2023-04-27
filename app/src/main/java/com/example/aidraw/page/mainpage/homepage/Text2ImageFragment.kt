package com.example.aidraw.page.mainpage.homepage

import android.content.Intent
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
import androidx.fragment.app.viewModels
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentTextToImageBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.page.mainpage.createpage.CreateImageActivity
import com.example.aidraw.pool.SpKey
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import com.example.aidraw.viewmodel.Text2ImageViewModel

class Text2ImageFragment:  Fragment() {

    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private val text2ImageViewModel: Text2ImageViewModel by activityViewModels()
    private lateinit var fragmentTextToImageBinding: FragmentTextToImageBinding
    private lateinit var text2ImagecreateButton: Button
    private lateinit var text2ImagepositionPromptLayout: ConstraintLayout
    private lateinit var text2ImagenegationPromptLayout: ConstraintLayout
    private lateinit var text2ImagepositionPromptInput: EditText
    private lateinit var text2ImagenegationPromptInput: EditText
    private lateinit var text2ImageinputOnFourceBackground: GradientDrawable


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTextToImageBinding = FragmentTextToImageBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentTextToImageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initData()
        initWidget()
    }

    private fun initWidget() {

        text2ImagepositionPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    text2ImagepositionPromptLayout.background  = text2ImageinputOnFourceBackground
                }
                false ->  {
                    text2ImagepositionPromptLayout.background  = null
                }
            }
        }

        text2ImagenegationPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    text2ImagenegationPromptLayout.background  = text2ImageinputOnFourceBackground
                }
                false ->  {
                    text2ImagenegationPromptLayout.background  = null
                }
            }
        }

        text2ImagecreateButton.setOnClickListener {
            text2ImagenegationPromptInput.clearFocus()
            text2ImagepositionPromptInput.clearFocus()
            val position = text2ImagepositionPromptInput.editableText.trimStart().trimEnd().toString()
            val negation = text2ImagenegationPromptInput.editableText.trimStart().trimEnd().toString()
            if(position.isNotBlank() && negation.isNotBlank()){
                val intent = Intent(requireContext() , CreateImageActivity::class.java)
                val text2ImageIntent = CreateImageIntent.Text2Image(
                    position = position,
                    negation = negation
                )
                intent.putExtra(SpKey.text_2_image , text2ImageIntent)
                startActivity(intent)
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
        text2ImagepositionPromptLayout = fragmentTextToImageBinding.text2ImagePositionPromptLayout
        text2ImagenegationPromptLayout = fragmentTextToImageBinding.text2ImageNegationPromptLayout
        text2ImagepositionPromptInput = fragmentTextToImageBinding.text2ImagePositionPromptInput
        text2ImagenegationPromptInput =  fragmentTextToImageBinding.text2ImageNegationPromptInput
        text2ImagecreateButton = fragmentTextToImageBinding.text2ImageCreateButton

        text2ImageinputOnFourceBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(this@Text2ImageFragment.requireContext() , R.color.green_ffd6),
                ContextCompat.getColor(this@Text2ImageFragment.requireContext() , R.color.blue_87ff),
                ContextCompat.getColor(this@Text2ImageFragment.requireContext() , R.color.purple_55ff)
            )
        ).apply {
            this.cornerRadius =  ExUtil.dip2px(this@Text2ImageFragment.requireContext() , 10f)
        }
        text2ImagepositionPromptInput.text = Editable.Factory.getInstance().newEditable("(masterpiece,best quality:1.6), (solo,1girl,bust:1.2), scenery,(Mountain, maid), flower,butterfly")
        text2ImagenegationPromptInput.text = Editable.Factory.getInstance().newEditable("nsfw,(worst quality,low quality,normal quality:1.6),a realistic face, impossible anatomy,impossible arms and legs, impossible hands and feet, impossible fingers and toes, impossible numbers of limbs and fingers, impossible clothes and shoes, watermark ")



    }


}