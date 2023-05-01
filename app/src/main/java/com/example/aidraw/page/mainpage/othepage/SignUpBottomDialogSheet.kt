package com.example.aidraw.page.mainpage.othepage

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentLogInBinding
import com.example.aidraw.databinding.FragmentSignUpBinding
import com.example.aidraw.intent.UserIntent
import com.example.aidraw.page.mainpage.MainActivity
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.state.UserState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpBottomDialogSheet: BottomSheetDialogFragment() {

    companion object{
        val TAG = SignUpBottomDialogSheet::class.java.name
    }

    private lateinit var fragmentSignUpBinding: FragmentSignUpBinding
    private lateinit var signUpUserName: EditText
    private lateinit var signUpPassword: EditText
    private lateinit var signUpEmail: EditText
    private lateinit var signUpPhone: EditText
    private lateinit var signUpConfirmPassword:EditText
    private lateinit var signUpUserNameLayout: FrameLayout
    private lateinit var signUpPasswordLayout: FrameLayout
    private lateinit var signUpEmailLayout: FrameLayout
    private lateinit var signUpPhoneLayout: FrameLayout
    private lateinit var signUpConfirmPasswordLayout: FrameLayout
    private lateinit var signUpButton: Button
    private lateinit var editTextFocusBg: GradientDrawable
    private val userViewModel:  UserViewModel by viewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ExUtil.setLinearGradientText(fragmentSignUpBinding.signUpTitle , requireContext())
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NORMAL , R.style.DialogBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSignUpBinding = FragmentSignUpBinding.inflate(
            inflater,
            container,
            false
        )
        if(ExUtil.hasNavigationBar(requireContext())){
            val layoutParams = fragmentSignUpBinding.signUpLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(0, 0 , 0 , ExUtil.getNavigationBarHeight(requireContext()))
        }
        return fragmentSignUpBinding.root
    }


    private fun  init(){
        initData()
        initWidget()
        handleState()
    }

    private  fun initData(){
        signUpUserName = fragmentSignUpBinding.signUpUsername
        signUpPassword = fragmentSignUpBinding.signUpPassword
        signUpEmail = fragmentSignUpBinding.signUpEmail
        signUpPhone = fragmentSignUpBinding.signUpPhone
        signUpConfirmPassword = fragmentSignUpBinding.signUpConfirmPassword
        signUpUserNameLayout = fragmentSignUpBinding.signUpUsernameLayout
        signUpPasswordLayout = fragmentSignUpBinding.signUpPasswordLayout
        signUpEmailLayout = fragmentSignUpBinding.signUpEmailLayout
        signUpPhoneLayout = fragmentSignUpBinding.signUpPhoneLayout
        signUpButton =  fragmentSignUpBinding.signUpButton
        signUpConfirmPasswordLayout = fragmentSignUpBinding.signUpConfirmPasswordLayout


        editTextFocusBg =   GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                ContextCompat.getColor(requireContext() , R.color.green_ffd6),
                ContextCompat.getColor(requireContext() , R.color.blue_87ff),
                ContextCompat.getColor(requireContext() , R.color.purple_55ff)
            )
        ).apply {
            this.cornerRadius =  ExUtil.dip2px(requireContext() , 30f)
        }
    }

    private fun  initWidget(){
        signUpUserName.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {
                    signUpUserNameLayout.background = editTextFocusBg
                }
                false -> {
                    signUpUserNameLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }
        signUpPassword.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {
                    signUpPasswordLayout.background = editTextFocusBg
                }
                false -> {
                    signUpPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }
        signUpConfirmPassword.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {
                    signUpConfirmPasswordLayout.background = editTextFocusBg
                }
                false -> {
                    signUpConfirmPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }
        signUpEmail.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {

                    signUpEmailLayout.background = editTextFocusBg
                }
                false -> {
                    signUpEmailLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }
        signUpPhone.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {

                    signUpPhoneLayout.background = editTextFocusBg
                }
                false -> {
                    signUpPhoneLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }
        signUpButton.setOnClickListener {
            signUpUserName.clearFocus()
            signUpPassword.clearFocus()
            signUpEmail.clearFocus()
            signUpPhone.clearFocus()
            signUpConfirmPassword.clearFocus()
            val username = signUpUserName.editableText.trimStart().trimEnd().toString()
            val password = signUpPassword.editableText.trimStart().trimEnd().toString()
            val confirmPassword = signUpConfirmPassword.editableText.trimStart().trimEnd().toString()
            val email = signUpEmail.editableText.trimStart().trimEnd().toString()
            val phone = signUpPhone.editableText.trimStart().trimEnd().toString()
            if(username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()){
                if(!password.equals(confirmPassword)){
                    signUpPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    signUpConfirmPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.password_confirm_password_tip
                    )
                }else{
                    userViewModel.post(
                        userIntent = UserIntent.addUser(
                            username, password, phone, email
                        )
                    )
                }
            }else{
                if(username.isBlank()){
                    signUpUserNameLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.username_tip
                    )
                }else if(password.isBlank()){
                    signUpPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.username_tip
                    )
                }else if(confirmPassword.isBlank()){
                    signUpConfirmPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.username_tip
                    )
                }
            }

        }
    }

    private fun handleState(){
        lifecycleScope.launch {
            userViewModel.userState.collect{
                if(it is UserState.queryingOrAdding){
                    loadingDialog.show(
                        parentFragmentManager,
                        LoadingDialog.TAG
                    )
                }else if(it is UserState.addUserSuccess){
                    loadingDialog.dismissAllowingStateLoss()
                    ExUtil.toast(
                        requireContext(),
                        R.string.sign_up_success
                    )
                }
            }
        }
    }
}