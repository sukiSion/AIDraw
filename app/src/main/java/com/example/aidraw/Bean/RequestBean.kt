package com.example.aidraw.Bean

import com.example.aidraw.pool.CachePool
import com.example.aidraw.pool.ConstantPool

data class RequestBean(
    var data: Array<Any> = listOf<Any>(
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
    var fn_index: Int = ConstantPool.fn_index_text2img,
    val session_hash: String
){

    fun setText2ImageData(position: String , negation: String){
        data = listOf<Any>(
            "task(9sppgt5q4hambk7)",
            position,
            negation,
            listOf<Any>(),
            CachePool.instance.steps,
            CachePool.instance.sampler,
            CachePool.instance.faceRepairSwitch,
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
            CachePool.instance.height,
            CachePool.instance.width,
            CachePool.instance.hdRepairSwitch,
            CachePool.instance.denoisiong,
            CachePool.instance.scale,
            "Latent",
            0,
            CachePool.instance.adjustmentWidth,
            CachePool.instance.adjustmentHeight,
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
        ).toTypedArray()
    }
}