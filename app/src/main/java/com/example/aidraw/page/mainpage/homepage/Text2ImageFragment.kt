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
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentTextToImageBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.page.mainpage.createpage.CreateImageActivity
import com.example.aidraw.page.mainpage.othepage.LoadingDialog
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.pool.SpKey
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.util.ImageUtil
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import com.example.aidraw.viewmodel.Text2ImageViewModel
import kotlinx.coroutines.launch
import java.io.File


class Text2ImageFragment:  Fragment() {

    private lateinit var fragmentTextToImageBinding: FragmentTextToImageBinding
    private lateinit var text2ImagecreateButton: Button
    private lateinit var text2ImagepositionPromptLayout: ConstraintLayout
    private lateinit var text2ImagenegationPromptLayout: ConstraintLayout
    private lateinit var text2ImagepositionPromptInput: EditText
    private lateinit var text2ImagenegationPromptInput: EditText
    private lateinit var text2ImageinputOnFourceBackground: GradientDrawable
    private lateinit var getImageInformationLayout: ConstraintLayout
    private lateinit var getImageInformationButton: Button
    private lateinit var uploadImage: ImageView
    private lateinit var launcher: ActivityResultLauncher<String>
    private val text2ImageViewModel: Text2ImageViewModel by activityViewModels()
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog() }

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
        handleState()
    }

    private fun initWidget() {

        text2ImageViewModel.uploadImagePath.observe(
            viewLifecycleOwner
        ){
            val file  = File(it)
            if(file.exists()){
                Glide.with(requireContext())
                    .load(file)
                    .into(uploadImage)
                getImageInformationButton.isEnabled = true
            }else{
                ExUtil.toast(
                    requireContext(),
                    R.string.image_no_exist
                )
            }
        }

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

        getImageInformationLayout.setOnClickListener {
            launcher.launch(ConstantPool.album_content)
        }

        getImageInformationButton.setOnClickListener {
            text2ImageViewModel.getUploadImagePath()?.apply {
                val file = File(this)
                if(file.exists()){
                    ImageUtil.imageToBase64(this)?.apply {
                        sdWebUICreateViewModel.post(
                            SDWebUICreateIntent.GetImageInformation(
                                "${ConstantPool.image_base64_scheme}${this}",
                                ExUtil.getAndroidId(requireContext())
                            )
                        )
                    }
                }
            }
        }


        text2ImageViewModel.currentPosition.observe(
            viewLifecycleOwner
        ){
            text2ImagepositionPromptInput.text = Editable.Factory.getInstance().newEditable(it)
        }

        text2ImageViewModel.currentNegation.observe(
            viewLifecycleOwner
        ){
            text2ImagenegationPromptInput.text = Editable.Factory.getInstance().newEditable(it)
        }

    }

    private fun initData() {
        text2ImagepositionPromptLayout = fragmentTextToImageBinding.text2ImagePositionPromptLayout
        text2ImagenegationPromptLayout = fragmentTextToImageBinding.text2ImageNegationPromptLayout
        text2ImagepositionPromptInput = fragmentTextToImageBinding.text2ImagePositionPromptInput
        text2ImagenegationPromptInput =  fragmentTextToImageBinding.text2ImageNegationPromptInput
        text2ImagecreateButton = fragmentTextToImageBinding.text2ImageCreateButton
        getImageInformationLayout = fragmentTextToImageBinding.getImageInformationLayout
        getImageInformationButton = fragmentTextToImageBinding.getImageInformationButton
        uploadImage = fragmentTextToImageBinding.uploadImage

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

        launcher = this.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){
            it?.let {
                val path = ImageUtil.getFilePathFromURI(requireContext() , it)
                path?.apply {
                    text2ImageViewModel.setUploadImagePath(this)
                }
            }
        }

    }

    private fun handleState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                it?.apply {
                    if(this is SDWebUICreateState.Creating){
                        loadingDialog.show(
                            parentFragmentManager,
                            LoadingDialog.TAG
                        )
                    }
                    else if(this is SDWebUICreateState.GetImageInformationSuccess){

                        text2ImageViewModel.getUploadImagePath()?.let {
                            val getImageInformationDialog =  GetImageInformationDialog(
                                positionPrompt = this.positionPrompt,
                                negationPrimpt = this.negationPrompt,
                                imagePath = it,
                                steps =  this.steps,
                                sampler = this.sampler,
                                cfgScale = this.cfgScale,
                                seed = this.seed,
                                width = this.width,
                                height = this.height,
                                modelHash = this.modelHash,
                                model = this.model,
                                clipSkip = this.clipSkip,
                                ensd = this.ensd
                            )
                            getImageInformationDialog.show(
                                parentFragmentManager,
                                GetImageInformationDialog.TAG
                            )
                        }

                        if(loadingDialog.isVisible){
                            loadingDialog.dismissAllowingStateLoss()
                        }
                    }
                    else if(this is SDWebUICreateState.GetImageInformationFail){
                        if(loadingDialog.isVisible){
                            loadingDialog.dismissAllowingStateLoss()
                        }
                    }
                    else if(this is SDWebUICreateState.ImageCreateError){
                        if(loadingDialog.isVisible){
                            loadingDialog.dismissAllowingStateLoss()
                        }

                    }
                }
            }
        }
    }


}