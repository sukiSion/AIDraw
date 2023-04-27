package com.example.aidraw.intent

import java.util.Base64

sealed interface SDWebUICreateIntent{

    class Text2Image(
        val positionPrompt: String ,
        val negationPrompt: String ,
        val seesionHash: String
    ): SDWebUICreateIntent

    class Image2Image(
        val positionPrompt: String ,
        val negationPrompt: String ,
        val imageBase64: String ,
        val seesionHash: String
    ): SDWebUICreateIntent

    class ClipReversePrompt(
        val imageBase64: String ,
        val seesionHash: String
    ): SDWebUICreateIntent


    class DeepBooruReversePrompt(
        val imageBase64: String ,
        val seesionHash: String
    ): SDWebUICreateIntent
}