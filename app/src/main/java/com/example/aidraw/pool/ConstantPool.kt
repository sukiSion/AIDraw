package com.example.aidraw.pool

import android.Manifest
import android.os.Environment
import com.example.aidraw.Bean.SamplarBean

object ConstantPool {

    const val appName = "AIDraw"

    const val SessionHash: String = "SessionHash"
    // t2i的fn_index
    const val fn_index_text2img = 77
    // clip反推提示词的fn_index
    const val fn_index_clip_reverse_prompt = 148
    // dedpbooru反推提示词的fn_index
    const val fn_index_deepbooru_reverse_prompt = 149
    // 图生图的fn_index
    const val fn_index_image2image = 146
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

    // 上传图片的Scheme头：
    const val image_base64_scheme = "data:image/png;base64,"
    // 图片后缀
    const val image_suffix = ".png"

    val album_dir = "${Environment.DIRECTORY_PICTURES}/$appName"
    val album_content = "image/*"

    val takePhotoPermission = Manifest.permission.CAMERA

    val saveFilePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

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