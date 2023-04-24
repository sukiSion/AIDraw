package com.example.aidraw.page.mainpage

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentHomeBinding
import com.example.aidraw.state.SDWebUIConfigState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SDWebUIViewModel
import kotlinx.coroutines.launch

class HomeFragment:  Fragment() {

    private val sdWebUIViewModel: SDWebUIViewModel by viewModels()
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
    }

    private fun handleSDWebUIState(){
        lifecycleScope.launch {
            sdWebUIViewModel.sdWebUIConfigState.collect{
                it?.let {
                    when(it){
                        is SDWebUIConfigState.text2imageResult -> {

                        }
                    }
                }
            }
        }

    }

}