package com.example.aidraw.state

import android.net.Uri

sealed interface OtherState {
    object downloading: OtherState
    data class downloadImageSuccess(val imageUri: Uri): OtherState
    data class shareImageSuccess(val imageUri: Uri): OtherState
    object refreshImageStart: OtherState
    data class refreshImageEnd(val imageUrl: String): OtherState
    class downloadError(e: Throwable): OtherState
}