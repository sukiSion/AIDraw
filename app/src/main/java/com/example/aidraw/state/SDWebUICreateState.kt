package com.example.aidraw.state

sealed interface SDWebUICreateState {
    class ImageCreateResult(val name: String):SDWebUICreateState
    class ImageCreateError(val exception: Throwable):SDWebUICreateState
    class ReversePromptSuccess(val prompt: String):SDWebUICreateState
    class GetImageInformationSuccess(
        val positionPrompt: String,
        val negationPrompt: String,
        val steps: Int,
        val sampler: String,
        val cfgScale: Int,
        val seed: Long,
        val width: Int,
        val height: Int,
        val modelHash: String,
        val model: String,
        val clipSkip: Int,
        val ensd: Int
    ): SDWebUICreateState
    object GetImageInformationFail: SDWebUICreateState
    object Creating : SDWebUICreateState
}
