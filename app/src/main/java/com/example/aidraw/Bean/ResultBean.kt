package com.example.aidraw.Bean

data class ResultBean(
    val data: List<Any>,
    val is_generating: Boolean,
    val duration:Float,
    val average_duration: Float
) {

}