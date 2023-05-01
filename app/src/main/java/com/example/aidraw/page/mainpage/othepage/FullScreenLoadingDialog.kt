package com.example.aidraw.page.mainpage.othepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.aidraw.R
import com.example.aidraw.databinding.DialogFullScreenLoadingBinding

class FullScreenLoadingDialog: DialogFragment() {

    companion object{
        val TAG = FullScreenLoadingDialog::class.java.name
    }

    private lateinit var dialogFullScreenLoadingBinding: DialogFullScreenLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogFullScreenLoadingBinding = DialogFullScreenLoadingBinding.inflate(
            inflater,
            container,
            false
        )
        dialog?.window?.apply {
            this.setBackgroundDrawableResource(R.color.transparent)
            this.decorView.setPadding(0 , 0 , 0 , 0)
            this.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return dialogFullScreenLoadingBinding.root
    }
}