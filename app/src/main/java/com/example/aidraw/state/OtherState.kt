package com.example.aidraw.state

import android.net.Uri

sealed interface OtherState {
    object Downloading: OtherState
    data class DownloadImageSuccess(val imageUri: Uri): OtherState
    data class ShareImageSuccess(val imageUri: Uri): OtherState
    object RefreshImageStart: OtherState
    data class RefreshImageEnd(val imageUrl: String): OtherState
    class DownloadError(e: Throwable): OtherState
}