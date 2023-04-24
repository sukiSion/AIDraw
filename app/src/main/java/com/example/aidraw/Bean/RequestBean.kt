package com.example.aidraw.Bean

import com.example.aidraw.pool.ConstantPool

data class RequestBean(
    val data: Array<Any> = listOf<Any>(
        "task(9sppgt5q4hambk7)",
        "",
        "",
        listOf<Any>(),
        50,
        "Euler a",
        false,
        false,
        1,
        1,
        7,
        -1,
        -1,
        0,
        0,
        0,
        false,
        512,
        512,
        true,
        0.7,
        2,
        "Latent",
        0,
        0,
        0,
        listOf<Any>(),
        "None",
        false,
        false,
        "positive",
        "comma",
        0,
        false,
        false,
        "",
        "Seed",
        "",
        "Nothing",
        "",
        "Nothing",
        "",
        true,
        false,
        false,
        false,
        0,
        listOf(
            FileBean().apply {
                this.setFileName("AIDraw_${System.currentTimeMillis()}")
            }
        ),
        "",
        "",
        ""
    ).toTypedArray(),
    val fn_index: Int = ConstantPool.fn_index,
    val session_hash: String
){
    fun setPosition(position: String){
        data[1] = position
    }
    fun setNavigation(navigation: String){
        data[2] = navigation
    }
}