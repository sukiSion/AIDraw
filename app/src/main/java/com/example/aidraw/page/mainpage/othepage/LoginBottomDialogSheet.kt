package com.example.aidraw.page.mainpage.othepage

import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aidraw.R
import com.example.aidraw.databinding.FragmentLogInBinding
import com.example.aidraw.intent.SDWebUICreateIntent
import com.example.aidraw.intent.UserIntent
import com.example.aidraw.page.mainpage.MainActivity
import com.example.aidraw.pool.ConstantPool
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.state.UserState
import com.example.aidraw.util.ExUtil
import com.example.aidraw.viewmodel.SDWebUICreateViewModel
import com.example.aidraw.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class LoginBottomDialogSheet: BottomSheetDialogFragment() {

    companion object{
        val TAG = LoginBottomDialogSheet::class.java.name
    }

    private lateinit var fragmentLogInBinding: FragmentLogInBinding
    private lateinit var loginUserNameLayout:FrameLayout
    private lateinit var loginUserName: EditText
    private lateinit var loginPasswordLayout: FrameLayout
    private lateinit var loginPassword: EditText
    private lateinit var loginSignUpText: TextView
    private lateinit var loginButton: Button
    private lateinit var editTextFocusBg: GradientDrawable
    private val userViewModel: UserViewModel by activityViewModels()
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        fragmentLogInBinding = FragmentLogInBinding.inflate(
            inflater,
            container,
            false
        )
        if(ExUtil.hasNavigationBar(requireContext())){
            val layoutParams = fragmentLogInBinding.loginLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.setMargins(0, 0 , 0 , ExUtil.getNavigationBarHeight(requireContext()))
        }
        return fragmentLogInBinding.root
    }

    private fun init(){
        initData()
        initWidget()
        handleState()
    }

    private fun initData(){
        loginUserNameLayout = fragmentLogInBinding.loginUsernameLayout
        loginPasswordLayout = fragmentLogInBinding.loginPasswordLayout
        loginUserName = fragmentLogInBinding.loginUsername
        loginPassword =  fragmentLogInBinding.loginPassword
        loginButton = fragmentLogInBinding.loginButton
        loginSignUpText = fragmentLogInBinding.loginSignUpText

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

    private fun initWidget(){
        ExUtil.setLinearGradientText(fragmentLogInBinding.loginTitle , requireContext())

        loginSignUpText.paint.setFlags(Paint. UNDERLINE_TEXT_FLAG)
        ExUtil.setLinearGradientText(loginSignUpText , requireContext())
        loginSignUpText.setOnClickListener {
            loginUserName.clearFocus()
            loginPassword.clearFocus()
            val signUpBottomDialogSheet =SignUpBottomDialogSheet()
            signUpBottomDialogSheet.show(
                parentFragmentManager,
                SignUpBottomDialogSheet.TAG
            )
            this.dismissAllowingStateLoss()
        }

        loginButton.setOnClickListener {
            loginUserName.clearFocus()
            loginPassword.clearFocus()
        }

        loginUserName.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {
                    loginUserNameLayout.background = editTextFocusBg
                }
                false -> {
                    loginUserNameLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }


        loginPassword.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true -> {
                    loginPasswordLayout.background = editTextFocusBg
                }
                false -> {
                    loginPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.login_sign_up_edittext_bg
                    )
                }
            }
        }

        loginButton.setOnClickListener {
            loginUserName.clearFocus()
            loginPassword.clearFocus()

            val username = loginUserName.editableText.trimStart().trimEnd().toString()
            val password = loginPassword.editableText.trimStart().trimEnd().toString()

            if(username.isNotBlank() && password.isNotBlank()){
                userViewModel.post(userIntent = UserIntent.queryUser(
                    username,password
                ))
            }else{
                if(username.isBlank()){
                    loginUserNameLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.username_tip
                    )
                }else if(password.isBlank()){
                    loginPasswordLayout.background = ContextCompat.getDrawable(
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

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    private fun handleState(){

        lifecycleScope.launch {
            userViewModel.userState.collect{
                if(it is UserState.queryingOrAdding){
                    loadingDialog.show(
                        parentFragmentManager,
                        LoadingDialog.TAG
                    )
                }else if(it is UserState.queryUserSuccess){
                    loadingDialog.dismissAllowingStateLoss()
                    ExUtil.toast(
                        requireContext(),
                        R.string.login_success
                    )
                }else if(it is UserState.queryUserIsNull){
                    loadingDialog.dismissAllowingStateLoss()
                    loginUserNameLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    loginPasswordLayout.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.erroe_edittext_bg
                    )
                    ExUtil.toast(
                        requireContext(),
                        R.string.username_password_error
                    )
                    loginUserName.text = Editable.Factory.getInstance().newEditable("")
                    loginPassword.text = Editable.Factory.getInstance().newEditable("")

                }
            }
        }
    }
}