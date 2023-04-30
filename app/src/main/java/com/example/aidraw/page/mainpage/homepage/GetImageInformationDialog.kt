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
    val steps: Int,
    val sampler: String,
    val cfgScale: Int,
    val seed: Long,
    val width: Int,
    val height: Int,
    val modelHash: String,
    val model: String,
    val clipSkip: Int,
    val ensd: Int
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
    private lateinit var stepsContent: TextView
    private lateinit var samplerContent: TextView
    private lateinit var cfgSclaeContent: TextView
    private lateinit var seedContent: TextView
    private lateinit var widthContent: TextView
    private lateinit var heightContent: TextView
    private lateinit var modelHashContent: TextView
    private lateinit var modelContent: TextView
    private lateinit var clipSkipContent: TextView
    private lateinit var ensdContent: TextView
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
        stepsContent = dialogGetImageInformationBinding.stepsContent
        cfgSclaeContent = dialogGetImageInformationBinding.cfgScaleContent
        seedContent = dialogGetImageInformationBinding.seedContent
        widthContent = dialogGetImageInformationBinding.widthContent
        modelContent = dialogGetImageInformationBinding.modelContent
        modelHashContent = dialogGetImageInformationBinding.modelHashContent
        clipSkipContent = dialogGetImageInformationBinding.clipSkipContent
        ensdContent = dialogGetImageInformationBinding.ensdContent
        samplerContent = dialogGetImageInformationBinding.samplerContent
        heightContent = dialogGetImageInformationBinding.heightContent
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
        dialogGetImageInformationBinding.stepsHeading.text = "${getString(R.string.steps_heading)} "
        dialogGetImageInformationBinding.samplerHeading.text = "${getString(R.string.sampler_heading)} "
        dialogGetImageInformationBinding.cfgScaleHeading.text = "${getString(R.string.cfg_scale_heading)} "
        dialogGetImageInformationBinding.seedHeading.text = "${getString(R.string.seed_heading)} "
        dialogGetImageInformationBinding.widthHeading.text = "${getString(R.string.width_heading)} "
        dialogGetImageInformationBinding.widthHeading.text = "${getString(R.string.height_heading)} "
        dialogGetImageInformationBinding.modelHashHeading.text = "${getString(R.string.model_hash_heading)} "
        dialogGetImageInformationBinding.modelHeading.text = "${getString(R.string.model_heading)} "
        dialogGetImageInformationBinding.clipSkipHeading.text = "${getString(R.string.clip_skip_heading)} "
        dialogGetImageInformationBinding.ensdHeading.text = "${getString(R.string.ensd_heading)} "
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.positionPromptHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.negationPromptHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.stepsHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.samplerHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.cfgScaleHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.seedHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.widthHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.heightHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.modelHashHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.modelHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.clipSkipHeading , requireContext())
        ExUtil.setLinearGradientText(dialogGetImageInformationBinding.ensdHeading , requireContext())

    }

    private fun init(){
        initData()
        initWidget()
        positionPromptContent.text = positionPrompt
        negationPromptContent.text = negationPrimpt
        stepsContent.text = "${steps}"
        cfgSclaeContent.text = "${cfgScale}"
        samplerContent.text = sampler
        modelHashContent.text = modelHash
        modelContent.text = model
        clipSkipContent.text = "${clipSkip}"
        ensdContent.text = "${ensd}"
        seedContent.text = "${seed}"
        widthContent.text = "${width}"
        heightContent.text = "${height}"
        Glide.with(requireContext())
            .load(imagePath)
            .into(getImageImformationImage)
    }
}