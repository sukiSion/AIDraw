package com.example.aidraw.page.mainpage.settingpage

import android.os.Bundle
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
import com.example.aidraw.databinding.DialogModelBinding
import com.example.aidraw.databinding.DialogSamplerBinding
import com.example.aidraw.pool.CachePool
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.viewmodel.SettingViewModel

class ModelDialog: DialogFragment() {

    companion object{
        val TAG = ModelDialog::class.java.name
    }

    private lateinit var dialogModelBinding: DialogModelBinding
    private lateinit var models: List<ModelBean>
    private var currentIndex = 0
    private val settingViewModel: SettingViewModel by activityViewModels()
    private lateinit var modelAdapter: ModelAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        models = CachePool.instance.supportModels.mapIndexed { index, s ->
            if(settingViewModel.getModel() == null){
                if(s.equals(CachePool.instance.model)){
                    ModelBean(s , true)
                }else{
                    ModelBean(s , false)
                }
            }else {
                if (s.equals(settingViewModel.getModel())) {
                    ModelBean(s , true)
                }else{
                    ModelBean(s , false)
                }
            }
        }


        models.forEachIndexed {
                index, modelBean ->
            if(modelBean.selected){
                currentIndex = index
                return@forEachIndexed
            }
        }

        modelAdapter = ModelAdapter(
            models,
            this.requireContext(),
        ){
                itemView, index ->
            if(currentIndex != index){
                models.forEach {
                        modelBean ->
                    modelBean.selected = false
                }
                models[index].selected = true
                currentIndex = index
                modelAdapter.notifyDataSetChanged()
            }
        }
        dialogModelBinding.modelList.adapter = modelAdapter
        dialogModelBinding.modelList.layoutManager =  LinearLayoutManager(
            this.requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        dialogModelBinding.modelDialogCloseIcon.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogModelBinding.modelCancelButton.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialogModelBinding.modelConfirmButton.setOnClickListener {
            settingViewModel.changeModel(models[currentIndex].name)
            this.dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogModelBinding = DialogModelBinding.inflate(
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
        return dialogModelBinding.root
    }


}