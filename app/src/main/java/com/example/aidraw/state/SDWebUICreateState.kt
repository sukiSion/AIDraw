package com.example.aidraw.state

import com.example.aidraw.Bean.ChangModelResultBean
import java.util.Base64

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

    data class ChangModelSuccess(val model: String) : SDWebUICreateState

    data class GetSupportModelSuccess(val models: List<String>): SDWebUICreateState
    data class InitAppSuccess(val aboutModelData: ChangModelResultBean): SDWebUICreateState

    data class PreprocessSuccess(val imageBase64s: List<String>) : SDWebUICreateState
    object Creating : SDWebUICreateState
}
