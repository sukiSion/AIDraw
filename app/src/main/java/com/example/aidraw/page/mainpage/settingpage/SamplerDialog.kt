package com.example.aidraw.page.mainpage.settingpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aidraw.Bean.ModelBean
import com.example.aidraw.Bean.SamplerBean
import com.example.aidraw.R
import com.example.aidraw.databinding.DialogSamplerBinding
import com.example.aidraw.pool.CachePool
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.viewmodel.SettingViewModel

class SamplerDialog: DialogFragment() {

    companion object{
        val TAG = SamplerDialog::class.java.name
    }

    private lateinit var samplers: List<SamplerBean>
    private val settingViewModel: SettingViewModel by activityViewModels()
    private var currentIndex = 0
    private lateinit var dialogSamplerBinding: DialogSamplerBinding
    private lateinit var samplerAdapter: SamplerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        samplers = ConstantPool.samplers.mapIndexed {
            index , s ->
            if(settingViewModel.getSampler() == null){
                if(s.equals(CachePool.instance.model)){
                    currentIndex = index
                    SamplerBean(s , true)
                }else{
                    SamplerBean(s , false)
                }
            }else {
                if (s.equals(settingViewModel.getSampler())) {
                    currentIndex = index
                    SamplerBean(s , true)
                }else{
                    SamplerBean(s , false)
                }
            }
        }



        samplerAdapter = SamplerAdapter(
            samplers,
            this.requireContext(),
        ){
            itemView, index ->
            if(currentIndex != index){
                samplers.forEach {
                        samplerBean ->
                    samplerBean.selected = false
                }
                samplers[index].selected = true
                currentIndex = index
                samplerAdapter.notifyDataSetChanged()
            }
        }
        dialogSamplerBinding.samplerList.adapter = samplerAdapter
        dialogSamplerBinding.samplerList.layoutManager =  LinearLayoutManager(
            this.requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        dialogSamplerBinding.samplerCancelButton.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogSamplerBinding.samplerDialogCloseIcon.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogSamplerBinding.samplerConfirmButton.setOnClickListener {
            settingViewModel.changeSampler(samplers[currentIndex].name)
            this.dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogSamplerBinding = DialogSamplerBinding.inflate(
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
        return dialogSamplerBinding.root
    }
}