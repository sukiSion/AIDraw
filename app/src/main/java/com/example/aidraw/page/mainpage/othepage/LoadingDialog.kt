package com.example.aidraw.page.mainpage.othepage

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.DialogFragment
import com.example.aidraw.R
import com.example.aidraw.databinding.DialogLoadingBinding

class LoadingDialog: DialogFragment() {

    companion object{
        val TAG = LoadingDialog::class.java.name
    }

    private lateinit var dialogLoadingBinding: DialogLoadingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ObjectAnimator.ofFloat(
            dialogLoadingBinding.loadingIcon,
            "rotation",
            0f , 360f
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogLoadingBinding = DialogLoadingBinding.inflate(
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
        isCancelable = false
        return dialogLoadingBinding.root
    }
}