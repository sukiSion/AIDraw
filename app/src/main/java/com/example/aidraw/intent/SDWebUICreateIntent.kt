package com.example.aidraw.intent

sealed interface SDWebUICreateIntent{

    class Text2Image(
        val positionPrompt: String ,
        val negationPrompt: String ,
        val seesionHash: String
    ): SDWebUICreateIntent

}