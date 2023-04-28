package com.example.aidraw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidraw.Bean.UserBean
import com.example.aidraw.MyApplication
import com.example.aidraw.intent.UserIntent
import com.example.aidraw.net.AppCoroutineExceptionHandler
import com.example.aidraw.room.UserDao
import com.example.aidraw.room.UserDataBase
import com.example.aidraw.state.SDWebUICreateState
import com.example.aidraw.state.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel  : ViewModel(){

    private val userDao: UserDao by lazy {
        MyApplication.userDataBase.userDao()
    }

    private val userChannel: Channel<UserIntent> = Channel(Channel.UNLIMITED)

    private val _userState: MutableStateFlow<UserState?> = MutableStateFlow(null)
    val userState : StateFlow<UserState?> = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            userChannel.consumeAsFlow().collect{
                when(it){
                    is UserIntent.addUser ->{
                        addUser(
                            username = it.username,
                            password = it.password,
                            phone = it.phone,
                            email = it.email
                        )
                    }
                    is UserIntent.queryUser -> {
                        queryUser(it.username , it.password)
                    }
                }
            }
        }
    }

    fun post(userIntent: UserIntent){
        viewModelScope.launch {
            userChannel.send(userIntent)
        }
    }

    private fun addUser(
        username:String,
        password: String,
        phone: String,
        email: String
    ){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _userState.emit(UserState.queryOrAddUserError(exception))
            }
        }){
            val  user =  UserBean(
                name = username,
                password = password,
                phone = phone,
                email = email
            )
            flow {
                emit(userDao.addUser(user))
            }
                .onStart {
                    _userState.emit(UserState.queryingOrAdding)
                }
                .flowOn(Dispatchers.IO)
                .collect{
                    _userState.emit(UserState.addUserSuccess(it))
                }
        }
    }


    private fun queryUser(
        username:String,
        password: String,
    ){
        viewModelScope.launch (context = this.viewModelScope.coroutineContext + AppCoroutineExceptionHandler{
                context: CoroutineContext, exception: Throwable ->
            viewModelScope.launch(Dispatchers.Main + context) {
                _userState.emit(UserState.queryOrAddUserError(exception))
            }
        }){
            userDao.queryUser(
                username,
                password
            ).onStart {
                _userState.emit(UserState.queryingOrAdding)
            }.flowOn(Dispatchers.IO)
                .collect{
                    if(it.isNotEmpty()){
                        _userState.emit(UserState.queryUserSuccess(it[0]))
                    }else{
                        _userState.emit(UserState.queryUserIsNull)
                    }
                }
        }
    }
}