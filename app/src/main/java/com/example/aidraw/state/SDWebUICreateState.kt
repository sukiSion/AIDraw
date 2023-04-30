package com.example.aidraw.state

sealed interface SDWebUICreateState {
    class ImageCreateResult(val name: String):SDWebUICreateState
    class ImageCreateError(val exception: Throwable):SDWebUICreateState
    class ReversePromptSuccess(val prompt: String):SDWebUICreateState
    class ImageInformation(
        val positionPrompt: String,
        val negationPrompt: String,

    ): SDWebUICreateState
    object Creating : SDWebUICreateState
}
