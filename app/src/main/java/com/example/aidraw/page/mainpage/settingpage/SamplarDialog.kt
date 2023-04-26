package com.example.aidraw.page.mainpage.settingpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aidraw.Bean.SamplarBean
import com.example.aidraw.R
import com.example.aidraw.databinding.DialogSamplarBinding
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.viewmodel.SettingViewModel

class SamplarDialog: DialogFragment() {

    companion object{
        val TAG = SamplarDialog::class.java.name
    }

    private lateinit var samplars: List<SamplarBean>
    private val settingViewModel: SettingViewModel by activityViewModels()
    private var currentIndex = 0
    private lateinit var dialogSamplarBinding: DialogSamplarBinding
    private lateinit var samplarAdapter: SamplarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        samplars = mutableListOf<SamplarBean>().apply {
            this.addAll(ConstantPool.samplers)
        }

        samplars.forEachIndexed {
            index, samplarBean ->
            if(samplarBean.selected){
                currentIndex = index
                return@forEachIndexed
            }
        }


        samplarAdapter = SamplarAdapter(
            samplars,
            this.requireContext(),
        ){
            itemView, index ->
            if(currentIndex != index){
                samplars.forEach {
                        samplarBean ->
                    samplarBean.selected = false
                }
                samplars[index].selected = true
                currentIndex = index
                samplarAdapter.notifyDataSetChanged()
            }
        }
        dialogSamplarBinding.samplarList.adapter = samplarAdapter
        dialogSamplarBinding.samplarList.layoutManager =  LinearLayoutManager(
            this.requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        dialogSamplarBinding.samplarCancelButton.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogSamplarBinding.samplarDialogCloseIcon.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogSamplarBinding.samplarConfirmButton.setOnClickListener {
            settingViewModel.changeSampler(samplars[currentIndex].name)
            this.dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogSamplarBinding = DialogSamplarBinding.inflate(
            inflater,
            container,
            false
        )
        dialog?.window?.let {
            it.setBackgroundDrawableResource(R.color.transparent)
            it.decorView.setPadding(0 , 0 , 0 ,  0)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return dialogSamplarBinding.root
    }
}