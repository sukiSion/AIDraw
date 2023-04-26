package com.example.aidraw.page.mainpage.settingpage

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentSettingBinding
import com.example.aidraw.pool.CachePool
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.pool.SpKey
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SettingViewModel
import com.tencent.mmkv.MMKV
import java.math.BigDecimal
import java.math.RoundingMode

class SettingFragment: Fragment() {

    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by activityViewModels()
    private lateinit var faceRepairRadioIcon: ImageView
    private lateinit var hdRepairRadioIcon: ImageView
    private lateinit var faceRepairLayout: ConstraintLayout
    private lateinit var hdRepairLayout: ConstraintLayout
    private lateinit var widthSeekBar: SeekBar
    private lateinit var heightSeekBar: SeekBar
    private lateinit var stepsSeekBar: SeekBar
    private lateinit var scaleSeekBar: SeekBar
    private lateinit var denoisingSeekBar: SeekBar
    private lateinit var currrentWidth: TextView
    private lateinit var currentHeight: TextView
    private lateinit var currentSteps: TextView
    private lateinit var currentScale: TextView
    private lateinit var currentSampler: EditText
    private lateinit var currrentDenoising: TextView
    private lateinit var widthChangeListener: SeekBar.OnSeekBarChangeListener
    private lateinit var heightChangeListener: SeekBar.OnSeekBarChangeListener
    private lateinit var scaleChangeListener: SeekBar.OnSeekBarChangeListener
    private lateinit var stepsChangeListener: SeekBar.OnSeekBarChangeListener
    private lateinit var denoisingChangeListener: SeekBar.OnSeekBarChangeListener
    private lateinit var hideSubmitTip: TextView

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
        faceRepairRadioIcon = fragmentSettingBinding.faceRepairRadioIcon
        hdRepairRadioIcon = fragmentSettingBinding.hdRepairRadioIcon
        faceRepairLayout = fragmentSettingBinding.faceRepairLayout
        hdRepairLayout = fragmentSettingBinding.hdRepairLayout
        widthSeekBar = fragmentSettingBinding.witdthSeekBar
        heightSeekBar = fragmentSettingBinding.heightSeekBar
        stepsSeekBar = fragmentSettingBinding.stepsSeekBar
        scaleSeekBar =  fragmentSettingBinding.scaleSeekBar
        denoisingSeekBar = fragmentSettingBinding.denoisingSeekBar
        currrentWidth = fragmentSettingBinding.width
        currentHeight = fragmentSettingBinding.height
        currentSteps = fragmentSettingBinding.steps
        currentScale = fragmentSettingBinding.scale
        currrentDenoising = fragmentSettingBinding.denoising
        currentSampler = fragmentSettingBinding.sampler
        hideSubmitTip = fragmentSettingBinding.hideSubmitTip

        if(MMKV.defaultMMKV().decodeBool(SpKey.submit_tip , true)){
            fragmentSettingBinding.submitTipLayout.visibility = View.VISIBLE
        }else{
            fragmentSettingBinding.submitTipLayout.visibility = View.GONE
        }

        scaleChangeListener =  object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = BigDecimal(((progress * 0.05f) + 1f).toDouble()).setScale(2, RoundingMode.HALF_UP).toFloat()
                settingViewModel.changeScale(value)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        stepsChangeListener = object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settingViewModel.changeSteps(progress + ConstantPool.min_steps)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        denoisingChangeListener = object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value: Float = (progress.toFloat() / 100)
                settingViewModel.changDenoising(value)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        widthChangeListener = object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settingViewModel.changeWidth(progress + ConstantPool.min_width_height)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        heightChangeListener = object : SeekBar.OnSeekBarChangeListener{


            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settingViewModel.changeHeight(progress + ConstantPool.min_width_height)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        settingViewModel.currentWidth.observe(
            viewLifecycleOwner
        ){
            currrentWidth.text = "${it}"
        }

        settingViewModel.currentHeight.observe(
            viewLifecycleOwner
        ){
            currentHeight.text = "${it}"
        }

        settingViewModel.currentSteps.observe(
            viewLifecycleOwner
        ){
            currentSteps.text = "${it}"
        }

        settingViewModel.currentDenoising.observe(
            viewLifecycleOwner
        ){
            currrentDenoising.text = "${it}"
        }

        settingViewModel.currentScale.observe(
            viewLifecycleOwner
        ){

            currentScale.text = "${it}"
        }

        settingViewModel.currentSampler.observe(
            viewLifecycleOwner
        ){
            currentSampler.text = Editable.Factory.getInstance().newEditable(it)
        }

        settingViewModel.faceRepairSelected.observe(
            viewLifecycleOwner
        ){
            when(it){
                true -> {
                    faceRepairRadioIcon.setImageDrawable(ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.icon_selected
                    ))
                }
                false -> {
                    faceRepairRadioIcon.setImageDrawable(ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.icon_noselect
                    ))
                }
            }
        }


        settingViewModel.hdRepairSelected.observe(
            viewLifecycleOwner
        ){
            when(it){
                true -> {
                    hdRepairRadioIcon.setImageDrawable(ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.icon_selected
                    ))
                }
                false -> {
                    hdRepairRadioIcon.setImageDrawable(ContextCompat.getDrawable(
                        this.requireContext(),
                        R.drawable.icon_noselect
                    ))
                }
            }
        }



    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initWidget(){

        ExUtil.setLinearGradientText(fragmentSettingBinding.widthTitle , this.requireContext())
        ExUtil.setLinearGradientText(fragmentSettingBinding.heightTitle , this.requireContext())
        ExUtil.setLinearGradientText(fragmentSettingBinding.stepsTitle , this.requireContext())
        ExUtil.setLinearGradientText(fragmentSettingBinding.scaleTitle , this.requireContext())
        ExUtil.setLinearGradientText(fragmentSettingBinding.denoisingTitle ,  this.requireContext())
        ExUtil.setLinearGradientText(fragmentSettingBinding.samplerTitle , this.requireContext())

        widthSeekBar.setOnSeekBarChangeListener(widthChangeListener)
        // 由于最小值是64,而每一次值得改变都是原本的值+64，所以最大值应改减去64
        widthSeekBar.max = ConstantPool.max_width_height - ConstantPool.min_width_height
        widthSeekBar.progress = CachePool.instance.width - ConstantPool.min_width_height

        heightSeekBar.setOnSeekBarChangeListener(heightChangeListener)
        heightSeekBar.max = ConstantPool.max_width_height - ConstantPool.min_width_height
        heightSeekBar.progress = CachePool.instance.height - ConstantPool.min_width_height

        stepsSeekBar.setOnSeekBarChangeListener(stepsChangeListener)
        stepsSeekBar.max = ConstantPool.max_steps - ConstantPool.min_steps
        stepsSeekBar.progress = CachePool.instance.steps - ConstantPool.min_steps

        denoisingSeekBar.setOnSeekBarChangeListener(denoisingChangeListener)
        denoisingSeekBar.max = ConstantPool.max_denoising
        denoisingSeekBar.progress = (CachePool.instance.denoisiong * 100).toInt()

        scaleSeekBar.setOnSeekBarChangeListener(scaleChangeListener)
        scaleSeekBar.max = (ConstantPool.max_scale - ConstantPool.min_scale).toInt() * (100 / 5)
        scaleSeekBar.progress = (CachePool.instance.scale - ConstantPool.min_scale ).toInt() * (100 / 5)

        hideSubmitTip.paint.setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        hideSubmitTip.paint.setAntiAlias(true);//抗锯齿
        hideSubmitTip.setOnClickListener {
            MMKV.defaultMMKV().encode(SpKey.submit_tip , false)
            fragmentSettingBinding.submitTipLayout.visibility = View.GONE
        }

        // 可以获取焦点但是不能弹出软键盘
        currentSampler.inputType = EditorInfo.TYPE_NULL
        // 当将 EditText 的 inputType 设置为 EditorInfo.TYPE_NULL 后，会导致 EditText 失去了获取焦点的能力，因此需要在代码中为其设置 setOnTouchListener，以便在用户点击时获取焦点。
        currentSampler.setOnTouchListener { _, _ ->
            currentSampler.requestFocusFromTouch()
            false
        }
        currentSampler.text = Editable.Factory.getInstance().newEditable(CachePool.instance.simpler)
        currentSampler.setOnClickListener {
            val  samplarDialog = SamplarDialog()
            samplarDialog.show(
                parentFragmentManager,
                SamplarDialog.TAG
            )
        }

        faceRepairLayout.setOnClickListener {
            settingViewModel.changeFaceRepairState()
        }
        hdRepairLayout.setOnClickListener {
            settingViewModel.changeHdRepairState()
        }
    }
}