package com.example.aidraw.intent

sealed interface UserIntent {

    data class addUser(
        val username:String,
        val password: String,
        val phone: String,
        val email: String
    ): UserIntent

    data class queryUser(
        val username: String,
        val password: String
    ): UserIntent
}