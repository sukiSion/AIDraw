package com.example.aidraw.page.mainpage.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentImageToImageBinding
import com.example.aidraw.intent.CreateImageIntent
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.page.mainpage.createpage.CreateImageActivity
import com.example.aidraw.page.mainpage.othepage.LoadingDialog
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.pool.SpKey
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.util.ImageUtil
import com.example.aidraw.util.SavePhotoUtil
import com.example.aidraw.viewmodel.Image2ImageViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import com.example.aidraw.viewmodel.Text2ImageViewModel
import kotlinx.coroutines.launch
import java.io.File

class Image2ImageFragment: Fragment() {

    private lateinit var fragmentImageToImageBinding: FragmentImageToImageBinding
    private lateinit var addImage: ImageView
    private lateinit var addImageLyout: ConstraintLayout
    private lateinit var clipReversePromptButton: Button
    private lateinit var deepbooruReversePromptButton: Button
    private lateinit var image2ImageCreateButton: Button
    private lateinit var image2ImagePositionPromptLayout: ConstraintLayout
    private lateinit var image2ImageNegationPromptLayout: ConstraintLayout
    private lateinit var image2ImagePositionPromptInput: EditText
    private lateinit var image2ImageNegationPromptInput: EditText
    private lateinit var imageImageinputOnFourceBackground: GradientDrawable
    private val image2ImageViewModel: Image2ImageViewModel by activityViewModels()
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
    private lateinit var openAlbumLauncher: ActivityResultLauncher<String>
    private lateinit var openCameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var takePhotoUri: Pair<Uri? , String?>? = null
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }
    private val selectImageBottomDialogSheet: SelectImageBottomDialogSheet by lazy {
        SelectImageBottomDialogSheet(
            selectAlbumItemCallback = {
                openAlbumLauncher.launch(ConstantPool.album_content)
            }
        ){
            permissionLauncher.launch(ConstantPool.takePhotoPermission)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentImageToImageBinding = FragmentImageToImageBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentImageToImageBinding.root
    }

    @SuppressLint("CheckResult")
    private fun initData(){
        addImageLyout = fragmentImageToImageBinding.addImageLayout
        addImage = fragmentImageToImageBinding.addImage
        clipReversePromptButton = fragmentImageToImageBinding.clipReversePromptButton
        deepbooruReversePromptButton = fragmentImageToImageBinding.deepbooruReversePromptButton
        image2ImageNegationPromptInput = fragmentImageToImageBinding.image2ImageNegationPromptInput
        image2ImagePositionPromptInput = fragmentImageToImageBinding.image2ImagePositionPromptInput
        image2ImagePositionPromptLayout = fragmentImageToImageBinding.image2ImagePositionPromptLayout
        image2ImageNegationPromptLayout = fragmentImageToImageBinding.image2ImageNegationPromptLayout
        image2ImageCreateButton = fragmentImageToImageBinding.image2ImageCreateButton

        imageImageinputOnFourceBackground = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(this@Image2ImageFragment.requireContext() , R.color.green_ffd6),
                ContextCompat.getColor(this@Image2ImageFragment.requireContext() , R.color.blue_87ff),
                ContextCompat.getColor(this@Image2ImageFragment.requireContext() , R.color.purple_55ff)
            )
        ).apply {
            this.cornerRadius =  ExUtil.dip2px(this@Image2ImageFragment.requireContext() , 10f)
        }

        openAlbumLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
        ){
            it?.let {
                selectImageBottomDialogSheet.takeIf {
                    it.isVisible
                }?.apply {
                    this.dismissAllowingStateLoss()
                }
                val path = ImageUtil.getFilePathFromURI(requireContext() , it)
                path?.apply {
                    if(File(this).exists()){
                        image2ImageViewModel.setCurrrentImagePath(this)
                    }
                }
            }
        }

        openCameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ){
            it?.apply {
                selectImageBottomDialogSheet.takeIf {
                    it.isVisible
                }?.apply {
                    this.dismissAllowingStateLoss()
                }
                takePhotoUri?.let {
                    it.second?.apply {
                        image2ImageViewModel.setCurrrentImagePath(this)
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){
            it?.apply {
                takePhotoUri = ImageUtil.getCameraUri(requireContext())
                takePhotoUri?.let {
                    it.first?.apply {
                        openCameraLauncher.launch(this)
                    }
                }
            }
        }

        image2ImageViewModel.currentImagePath.observe(
            viewLifecycleOwner
        ){
            val file = File(it)
            if(file.exists()){
                Glide.with(requireContext())
                    .load(file)
                    .apply{
                        RequestOptions.bitmapTransform(
                            RoundedCorners(ExUtil.dip2px(
                            requireContext(),
                            8f
                        ).toInt())
                        )
                    }
                    .into(addImage)
                image2ImageCreateButton.isEnabled = true
            }else{
                ExUtil.toast(
                    requireContext(),
                    R.string.image_no_exist
                )
            }
        }
    }

    // EditText取消焦点
    private fun clearFocus(){
        image2ImagePositionPromptInput.clearFocus()
        image2ImageNegationPromptInput.clearFocus()
    }


    private fun initWidget(){

        image2ImagePositionPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    image2ImagePositionPromptLayout.background  = imageImageinputOnFourceBackground
                }
                false ->  {
                    image2ImagePositionPromptLayout.background  = null
                }
            }
        }


        image2ImageNegationPromptInput.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus) {
                true -> {
                    image2ImageNegationPromptLayout.background  = imageImageinputOnFourceBackground
                }
                false ->  {
                    image2ImageNegationPromptLayout.background  = null
                }
            }
        }

        addImageLyout.setOnClickListener {
            clearFocus()
            if(ExUtil.isSoftKeyboardVisible(requireActivity())){
                ExUtil.closeKeyboard(requireActivity())
            }
            selectImageBottomDialogSheet.show(
                parentFragmentManager,
                SelectImageBottomDialogSheet.TAG
            )
        }
        clipReversePromptButton.setOnClickListener {
            clearFocus()
            if(image2ImageViewModel.getCurrentImagePath() == null){
                ExUtil.toast(
                    requireContext(),
                    R.string.please_select_image
                )
            }else{
                image2ImageViewModel.getCurrentImagePath()?.let {
                    val file = File(it)
                    if(file.exists()){
                        val imageBase64 = ImageUtil.imageToBase64(it)
                        imageBase64?.let {
                            sdWebUICreateViewModel.post(
                                SDWebUICreateIntent.ClipReversePrompt(
                                    sessionHash = ExUtil.getAndroidId(requireContext()),
                                    imageBase64 = "${ConstantPool.image_base64_scheme}${it}"
                                )
                            )
                        }
                    }
                }
            }
        }
        deepbooruReversePromptButton.setOnClickListener {
            clearFocus()
            if(image2ImageViewModel.getCurrentImagePath() == null){
                ExUtil.toast(
                    requireContext(),
                    R.string.please_select_image
                )
            }else{
                image2ImageViewModel.getCurrentImagePath()?.let {
                    val file = File(it)
                    if(file.exists()){
                        val imageBase64 = ImageUtil.imageToBase64(it)
                        imageBase64?.let {
                            sdWebUICreateViewModel.post(
                                SDWebUICreateIntent.DeepBooruReversePrompt(
                                    sessionHash = ExUtil.getAndroidId(requireContext()),
                                    imageBase64 = "${ConstantPool.image_base64_scheme}${it}"
                                )
                            )
                        }
                    }
                }
            }
        }
        image2ImageCreateButton.setOnClickListener {
            clearFocus()
            val position = image2ImagePositionPromptInput.editableText.trimStart().trimEnd().toString()
            val negation = image2ImageNegationPromptInput.editableText.trimStart().trimEnd().toString()
            if(position.isNotBlank() && negation.isNotBlank()){
                image2ImageViewModel.getCurrentImagePath()?.apply {
                    if(File(this).exists()){
                        val intent = Intent(requireContext() , CreateImageActivity::class.java)
                        val image2ImageIntent = CreateImageIntent.Image2Image(
                            position = position,
                            negation = negation,
                            path = this
                        )
                        intent.putExtra(SpKey.image_2_image , image2ImageIntent)
                        startActivity(intent)
                    }else{
                        ExUtil.toast(
                            requireContext(),
                            R.string.image_no_exist
                        )
                    }
                }
            }else{
                if(position.isBlank()){
                    ExUtil.toast(
                        requireContext(),
                        R.string.input_position_tip
                    )
                }else if(negation.isBlank()){
                    ExUtil.toast(
                        requireContext(),
                        R.string.input_negation_tip
                    )
                }
            }
        }
        image2ImageViewModel.currentPosition.observe(
            viewLifecycleOwner
        ){
            image2ImagePositionPromptInput.text = Editable.Factory.getInstance().newEditable(it)
        }
        image2ImageViewModel.currentNegation.observe(
            viewLifecycleOwner
        ){
            image2ImageNegationPromptInput.text = Editable.Factory.getInstance().newEditable(it)
        }
    }

    private fun init(){
        initData()
        initWidget()
        handleSDWebUIState()
    }

    private fun handleSDWebUIState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                it?.let {
                    if(it is SDWebUICreateState.Creating){
                        loadingDialog.show(
                            parentFragmentManager,
                            LoadingDialog.TAG
                        )
                    }else if(it is SDWebUICreateState.ReversePromptSuccess){
                        if(loadingDialog.isVisible){
                            loadingDialog.dismissAllowingStateLoss()
                        }
                        image2ImagePositionPromptInput.text = Editable.Factory.getInstance().newEditable(it.prompt)
                    }
                }
            }
        }
    }
}