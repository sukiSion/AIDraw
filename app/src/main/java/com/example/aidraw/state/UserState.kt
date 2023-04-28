package com.example.aidraw.state

import com.example.aidraw.Bean.UserBean

sealed interface UserState {
    data class queryUserSuccess(val userBean: UserBean): UserState
    object queryUserIsNull:  UserState
    data class queryOrAddUserError(val e: Throwable): UserState
    data class addUserSuccess(val id: Long): UserState
    object queryingOrAdding: UserState
}