package com.example.aidraw.state

sealed interface SDWebUICreateState {
    class Text2ImageResult(val name: String):SDWebUICreateState
    class Text2ImageError(val exception: Throwable):SDWebUICreateState
}
