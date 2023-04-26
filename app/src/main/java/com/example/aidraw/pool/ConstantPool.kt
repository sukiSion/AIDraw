package com.example.aidraw.pool

import com.example.aidraw.Bean.SamplarBean

object ConstantPool {
    const val SessionHash: String = "SessionHash"
    // t2i的fn_index
    const val fn_index_text2img = 77
    // 开启高清修复的fn_index
    const val fn_index_open_hd = 80
    // 设置高清修复配置的fn_index
    const val fn_index_hd_config = 58
    // 调整宽度的fn_index
    const val  fn_index_width = 60
    // 调整高度的fn_index
    const val fn_index_height = 62
    // 调整放大倍率的fn_index
    const val fn_index_scale = 64
    // 高度和宽度的最小值
    const val min_width_height = 64
    // 高度和宽带的最大值
    const val max_width_height = 2048
    // 最小采样迭代步数
    const val min_steps = 1
    // 最大采样迭代步数
    const val max_steps = 150
    // 最小放大倍数
    const val min_scale =  1f
    // 最大放大倍数
    const val max_scale = 4f
    // 最大重绘幅度
    const val max_denoising = 100
    // 改变高清修复后data中type的结果
    const val type =  "update"


    // 图片后缀
    const val image_suffix = ".png"

    val samplers = listOf<SamplarBean>(
        SamplarBean("Euler a" , true),
        SamplarBean("Euler" , false),
        SamplarBean("LMS" , false),
        SamplarBean("Heun" , false),
        SamplarBean("DPM2" , false),
        SamplarBean("DPM2 a" , false),
        SamplarBean("DPM++ 2S a" , false),
        SamplarBean("DPM++ 2M" , false),
        SamplarBean("DPM++ SDE" , false),
        SamplarBean("DPM fast" , false),
        SamplarBean("DPM adaptive" , false),
        SamplarBean("LMS Karras" , false),
        SamplarBean("DPM2 Karras" , false),
        SamplarBean("DPM2 a Karras" , false),
        SamplarBean("DPM++ 2S a Karras" , false),
        SamplarBean("DPM++ 2M Karras" , false),
        SamplarBean("DPM++ SDE Karras" , false),
        SamplarBean("DDIM" , false),
        SamplarBean("PLMS" , false),
    )
}