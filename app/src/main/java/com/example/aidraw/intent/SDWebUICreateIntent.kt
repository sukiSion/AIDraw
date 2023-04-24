package com.example.aidraw.intent

sealed interface SDWebUICreateIntent{

    class text2Image(
        val positionPrompt: String ,
        val navigationPrompt: String ,
        val seesionHash: String
    ): SDWebUICreateIntent

}