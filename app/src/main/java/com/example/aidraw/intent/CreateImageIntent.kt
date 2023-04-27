package com.example.aidraw.intent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface CreateImageIntent{

    @Parcelize
    data class Text2Image(val position:String , val negation: String): Parcelable, CreateImageIntent

    @Parcelize
    data class Image2Image(val position: String , val negation: String , val path: String) : Parcelable, CreateImageIntent

}

