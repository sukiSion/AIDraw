package com.example.aidraw.page.mainpage.homepage

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentControlNetBinding
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.page.mainpage.othepage.LoadingDialog
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.util.ImageUtil
import com.example.aidraw.viewmodel.ControlNetViewModel
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import kotlinx.coroutines.launch
import java.io.File

class ControlNetFragment: Fragment() {

    private lateinit var controlNetAddImageLayout: ConstraintLayout
    private lateinit var controlNetAddImage: ImageView
    private lateinit var preprocessingButton: Button
    private lateinit var cannyRadioLayout: LinearLayoutCompat
    private lateinit var segRadioLayout: LinearLayoutCompat
    private lateinit var leresRadioLayout: LinearLayoutCompat
    private lateinit var controlNetImageTextLayout: LinearLayoutCompat
    private lateinit var cannyRadioIcon: ImageView
    private lateinit var segRadioIcon: ImageView
    private lateinit var leresRadioIcon: ImageView
    private lateinit var cannyLayou: LinearLayoutCompat
    private lateinit var segLayout: LinearLayoutCompat
    private lateinit var leresLayout: LinearLayoutCompat
    private lateinit var cannyTitle: TextView
    private lateinit var segTitle: TextView
    private lateinit var leresTitle: TextView
    private lateinit var cannyImage: ImageView
    private lateinit var segImage: ImageView
    private lateinit var leresImage: ImageView


    private val controlNetViewModel: ControlNetViewModel by activityViewModels()
    private lateinit var fragmentControlNetBinding: FragmentControlNetBinding
    private lateinit var openAlbumLauncher: ActivityResultLauncher<String>
    private lateinit var openCameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val sdWebUICreateViewModel: SDWebUICreateViewModel by viewModels()
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
        fragmentControlNetBinding = FragmentControlNetBinding.inflate(
            inflater,
            container,
            false
        )
        return fragmentControlNetBinding.root
    }

    private fun init(){
        initData()
        initWidget()
        handleSDWebUIState()
    }

    fun initData(){

        controlNetAddImage = fragmentControlNetBinding.controlNetAddImage
        controlNetAddImageLayout = fragmentControlNetBinding.contrlNetAddImageLayout
        preprocessingButton = fragmentControlNetBinding.preprocessingButton
        controlNetImageTextLayout = fragmentControlNetBinding.controlNetImageTextLayout

        cannyRadioLayout = fragmentControlNetBinding.cannyRadioLayout
        cannyRadioIcon = fragmentControlNetBinding.cannyRadioIcon
        segRadioLayout = fragmentControlNetBinding.segRadioLayout
        segRadioIcon = fragmentControlNetBinding.segRadioIcon
        leresRadioLayout = fragmentControlNetBinding.leresRadioLayout
        leresRadioIcon = fragmentControlNetBinding.leresRadioIcon

        segLayout = fragmentControlNetBinding.segLayout
        segTitle = fragmentControlNetBinding.segTitle
        segImage = fragmentControlNetBinding.segImage

        cannyLayou = fragmentControlNetBinding.cannyLayout
        cannyTitle = fragmentControlNetBinding.cannyTitle
        cannyImage = fragmentControlNetBinding.cannyImage

        leresLayout = fragmentControlNetBinding.leresLayout
        leresTitle = fragmentControlNetBinding.leresTitle
        leresImage = fragmentControlNetBinding.leresImage

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
                        controlNetViewModel.setContrlNetImagePath(this)
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
                        controlNetViewModel.setContrlNetImagePath(this)
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

        controlNetViewModel.controlNetImagePath.observe(
            viewLifecycleOwner
        ){
            val file = File(it)
            if(file.exists()){
                Glide.with(requireContext())
                    .load(file)
                    .into(controlNetAddImage)
                preprocessingButton.isEnabled = true
                controlNetImageTextLayout.visibility = View.GONE
            }else{
                ExUtil.toast(
                    requireContext(),
                    R.string.image_no_exist
                )
                controlNetImageTextLayout.visibility = View.VISIBLE
            }
        }

        controlNetViewModel.cannySelected.observe(
            viewLifecycleOwner
        ){
            when(it){
                true -> {
                    cannyRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_selected
                        )
                    )
                }
                false -> {
                    cannyRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_noselect_blue
                        )
                    )
                }
            }
        }

        controlNetViewModel.segSelected.observe(
            viewLifecycleOwner
        ){
            when(it){
                true -> {
                    segRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_selected
                        )
                    )
                }
                false -> {
                    segRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_noselect_blue
                        )
                    )
                }
            }
        }

        controlNetViewModel.leResSelected.observe(
            viewLifecycleOwner
        ){
            when(it){
                true -> {
                    leresRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_selected
                        )
                    )
                }
                false -> {
                    leresRadioIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.icon_noselect_blue
                        )
                    )
                }
            }
        }

        preprocessingButton.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    fun initWidget(){
        cannyTitle.text = "${getString(R.string.canny_image)} "
        segTitle.text = "${getString(R.string.seg_image)} "
        leresTitle.text = "${getString(R.string.leres_image)} "
        ExUtil.setLinearGradientText(cannyTitle , requireContext())
        ExUtil.setLinearGradientText(segTitle , requireContext())
        ExUtil.setLinearGradientText(leresTitle , requireContext())

        cannyRadioLayout.setOnClickListener {
            controlNetViewModel.changeCannyState()
        }

        segRadioLayout.setOnClickListener {
            controlNetViewModel.changeSegState()
        }

        leresRadioLayout.setOnClickListener {
            controlNetViewModel.changeLeResState()
        }

        preprocessingButton.setOnClickListener {
            controlNetViewModel.getControlImagePath()?.let {
                val file = File(it)
                if(file.exists()){
                    val imageBase64 = ImageUtil.imageToBase64(it)
                    imageBase64?.let{
                        image ->
                        val base64 = "${ConstantPool.image_base64_scheme}${image}"
                        val preprocessors: List<Pair<String , String>> = mutableListOf<Pair<String , String>>().run {
                            controlNetViewModel.getCanny()?.apply {
                                if(this){
                                    this@run.add(base64 to ConstantPool.canny_preprocessor)
                                }
                            }
                            controlNetViewModel.getSeg()?.apply {
                                if(this){
                                    this@run.add(base64 to ConstantPool.seg_preprocessor)
                                }
                            }
                            controlNetViewModel.getLeRes()?.apply {
                                if(this){
                                    this@run.add(base64 to ConstantPool.leres_preprocessor)
                                }
                            }
                            this@run
                        }
                        preprocessors.isNotEmpty().apply {
                            if(this){
                                sdWebUICreateViewModel.post(SDWebUICreateIntent.Preprocess(
                                    preprocesses = preprocessors,
                                    sessionHash = ExUtil.getAndroidId(requireContext())
                                ))
                            }
                        }
                    }

                }
            }
        }

        controlNetAddImageLayout.setOnClickListener {
            selectImageBottomDialogSheet.show(
                parentFragmentManager,
                SelectImageBottomDialogSheet.TAG
            )
        }

    }

    fun handleSDWebUIState(){
        lifecycleScope.launch {
            sdWebUICreateViewModel.sdWebUICreateState.collect{
                if(it is SDWebUICreateState.Creating){
                    loadingDialog.show(
                        parentFragmentManager,
                        LoadingDialog.TAG
                    )
                }else if(it is SDWebUICreateState.ImageCreateError){
                    if(loadingDialog.isVisible){
                        loadingDialog.dismissAllowingStateLoss()
                    }
                    ExUtil.toast(
                        requireContext(),
                        R.string.network_error
                    )
                }
                else if(it is SDWebUICreateState.PreprocessSuccess){
                    if(loadingDialog.isVisible){
                        loadingDialog.dismissAllowingStateLoss()
                    }
                    it.imageBase64s.forEachIndexed { index, s ->
                        val cacheImagePath = ImageUtil.getCacheImagePath(requireContext())
                        val decode = ImageUtil.base64ToFile(s , cacheImagePath)
                        if(decode){
                            when(index){
                                0 -> {
                                    controlNetViewModel.getCanny()?.let {
                                        if(it){
                                            cannyLayou.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(cannyImage)
                                            return@forEachIndexed
                                        }
                                    }
                                    controlNetViewModel.getSeg()?.let {
                                        if(it){
                                            segLayout.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(segImage)
                                        }
                                    }

                                    controlNetViewModel.getLeRes()?.let {
                                        if(it){
                                            leresLayout.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(leresImage)
                                        }
                                    }
                                }
                                1 -> {
                                    controlNetViewModel.getSeg()?.let {
                                        if(it){
                                            segLayout.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(segImage)
                                        }
                                        return@forEachIndexed
                                    }

                                    controlNetViewModel.getLeRes()?.let {
                                        if(it){
                                            leresLayout.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(leresImage)
                                        }
                                    }
                                }
                                2 -> {

                                    controlNetViewModel.getLeRes()?.let {
                                        if(it){
                                            leresLayout.visibility = View.VISIBLE
                                            Glide.with(requireContext())
                                                .load(File(cacheImagePath))
                                                .into(leresImage)
                                        }
                                        return@forEachIndexed
                                    }
                                }
                            }
                            controlNetViewModel.getCanny()?.let {
                                if(it){
                                    cannyLayou.visibility = View.VISIBLE
                                    Glide.with(requireContext())
                                        .load(File(cacheImagePath))
                                        .into(cannyImage)
                                }
                            }
                            controlNetViewModel.getSeg()?.let {
                                if(it){
                                    segLayout.visibility = View.VISIBLE
                                    Glide.with(requireContext())
                                        .load(File(cacheImagePath))
                                        .into(segImage)
                                }
                            }

                            controlNetViewModel.getLeRes()?.let {
                                if(it){
                                    leresLayout.visibility = View.VISIBLE
                                    Glide.with(requireContext())
                                        .load(File(cacheImagePath))
                                        .into(leresImage)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}