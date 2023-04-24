package com.example.aidraw.page.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aidraw.databinding.FragmentSettingBinding
import com.example.aidraw.viewmodel.SDWebUIViewModel

class SettingFragment: Fragment() {

    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private val sdWebUIViewModel: SDWebUIViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingBinding = FragmentSettingBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initData()
        initWidget()
    }

    private fun initData(){

    }

    private fun initWidget(){

    }
}