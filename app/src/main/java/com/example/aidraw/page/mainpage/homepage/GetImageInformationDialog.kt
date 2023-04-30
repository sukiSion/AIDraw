package com.example.aidraw.page.mainpage.homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.aidraw.R
import com.example.aidraw.databinding.DialogGetImageInformationBinding
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.Image2ImageViewModel
import com.example.aidraw.viewmodel.MainViewModel
import com.example.aidraw.viewmodel.Text2ImageViewModel

class GetImageInformationDialog(
    val imagePath : String,
    val positionPrompt: String,
    val negationPrimpt: String,
): DialogFragment() {

    companion object{
        val  TAG = GetImageInformationDialog::class.java.name
    }

    private lateinit var dialogGetImageInformationBinding: DialogGetImageInformationBinding
    private lateinit var closrIcon: ImageView
    private lateinit var getImageImformationImage: ImageView
    private lateinit var goToText2Image: Button
    private lateinit var goToImage2Image: Button
    private lateinit var positionPromptContent: TextView
    private lateinit var negationPromptContent: TextView
    private val text2ImageViewModel:Text2ImageViewModel by activityViewModels()
    private val image2ImageViewModel:Image2ImageViewModel by activityViewModels()
    private val mainViewModel:MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogGetImageInformationBinding = DialogGetImageInformationBinding.inflate(
            inflater,
            container,
            false
        )
        dialog?.window?.apply {
            this.setBackgroundDrawableResource(R.color.transparent)
            this.decorView.setPadding(0 , 0 , 0,  0)
            this.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return dialogGetImageInformationBinding.root
    }

    private fun initData(){
        closrIcon = dialogGetImageInformationBinding.getImageInformationDialogCloseIcon.apply {
            setOnClickListener {
                this@GetImageInformationDialog.dismissAllowingStateLoss()
            }
        }
        getImageImformationImage = dialogGetImageInformationBinding.getImageInformationImage
        positionPromptContent = dialogGetImageInformationBinding.positionPromptContent
        negationPromptContent = dialogGetImageInformationBinding.negationPromptContent
        goToText2Image = dialogGetImageInformationBinding.goToText2Image
        goToImage2Image = dialogGetImageInformationBinding.goToImage2Image
    }

    @SuppressLint("SetTextI18n")
    private fun initWidget(){
        goToText2Image.setOnClickListener {
            text2ImageViewModel.setPosition(positionPrompt)
            text2ImageViewModel.setNegation(negationPrimpt)
            this.dismissAllowingStateLoss()
        }
        goToImage2Image.setOnClickListener {
            image2ImageViewModel.setPosition(positionPrompt)
            image2ImageViewModel.setNegation(negationPrimpt)
            image2ImageViewModel.setCurrrentImagePath(path = imagePath)
            mainViewModel.setCurrentPageIndex(1)
            this.dismissAllowingStateLoss()
        }
        dialogGetImageInformationBinding.positionPromptHeading.text = "${getString(R.string.position_prompt)} "
        dialogGetImageInformationBinding.negationPromptHeading.text = "${getString(R.string.negation_prompt)} "
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.positionPromptHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.negationPromptHeading , requireContext())

    }

    private fun init(){
        initData()
        initWidget()
        positionPromptContent.setText(positionPrompt)
        negationPromptContent.setText(negationPrimpt)
        Glide.with(requireContext())
            .load(imagePath)
            .into(getImageImformationImage)
    }
}