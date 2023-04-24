package com.example.aidraw.state

sealed interface SDWebUICreateState {
    class text2imageResult(val name: String):SDWebUICreateState

}