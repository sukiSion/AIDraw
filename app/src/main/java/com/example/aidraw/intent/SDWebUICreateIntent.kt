package com.example.aidraw.intent

import java.util.Base64

sealed interface SDWebUICreateIntent{

    class Text2Image(
        val positionPrompt: String ,
        val negationPrompt: String ,
        val sessionHash: String
    ): SDWebUICreateIntent

    class GetImageInformation(
        val imageBase64: String,
        val sessionHash: String
    ): SDWebUICreateIntent

    class Image2Image(
        val positionPrompt: String ,
        val negationPrompt: String ,
        val imageBase64: String ,
        val sessionHash: String
    ): SDWebUICreateIntent

    class ClipReversePrompt(
        val imageBase64: String ,
        val sessionHash: String
    ): SDWebUICreateIntent

    class DeepBooruReversePrompt(
        val imageBase64: String ,
        val sessionHash: String
    ): SDWebUICreateIntent

    class ChangModel(
        val model: String ,
        val sessionHash: String
    ): SDWebUICreateIntent

    class GetSupportModels(
        val sessionHash: String
    ): SDWebUICreateIntent

    class InitApp(
        val sessionHash: String
    ): SDWebUICreateIntent
}