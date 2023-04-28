package com.example.aidraw.intent

sealed interface OtherIntent{
    data class shareImage(val imageUrl: String): OtherIntent
    data class downloadImage(val imageUrl: String): OtherIntent
    object refreshImageStart: OtherIntent
    data class refreshImageEnd(val imageUrl: String): OtherIntent
}