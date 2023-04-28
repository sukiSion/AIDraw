package com.example.aidraw.page.mainpage.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentSelectImageBinding
import com.example.aidraw.util.ExUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectImageBottomDialogSheet(
    val selectAlbumItemCallback: () -> Unit,
    val selectCameraItemCallback: () -> Unit
): BottomSheetDialogFragment() {

    companion object{
        val TAG = SelectImageBottomDialogSheet::class.java.name
    }

    private lateinit var fragmentSelectImageBinding: FragmentSelectImageBinding
    private lateinit var selectAlbumItem:LinearLayoutCompat
    private lateinit var selectCameraItem:LinearLayoutCompat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectCameraItem = fragmentSelectImageBinding.selectCameraItem
        selectAlbumItem = fragmentSelectImageBinding.selectAlbumItem
        ExUtil.setLinearGradientText(fragmentSelectImageBinding.albumText , requireContext())
        ExUtil.setLinearGradientText(fragmentSelectImageBinding.cameraText , requireContext())
        selectAlbumItem.setOnClickListener {
            selectAlbumItemCallback()
        }
        selectCameraItem.setOnClickListener {
            selectCameraItemCallback()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NORMAL , R.style.DialogBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSelectImageBinding = FragmentSelectImageBinding.inflate(
            inflater,
            container,
            false
        )
        if(ExUtil.hasNavigationBar(requireContext())){
            val layoutParams = fragmentSelectImageBinding.selectAlbumCameraLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(0 , 0 , 0 , ExUtil.getNavigationBarHeight(requireContext()))
        }
        return fragmentSelectImageBinding.root
    }

}