package com.example.aidraw.pool

import android.Manifest
import android.os.Environment
import com.example.aidraw.Bean.SamplerBean

object ConstantPool {

    const val appName = "AIDraw"

    const val SessionHash: String = "SessionHash"
    // t2i的fn_index
    const val fn_index_text2img = 167
    // clip反推提示词的fn_index
    const val fn_index_clip_reverse_prompt = 352
    // dedpbooru反推提示词的fn_index
    const val fn_index_deepbooru_reverse_prompt = 353
    // 图生图的fn_index
    const val fn_index_image2image = 350
    // 切换模型的fn_index,需要改动
    const val fn_index_model = 241
    // 获取当前支持模型的fn_index
    const val fn_index_support_models = 0
    // 初始化WebUI数据的fn_index
    const val fn_index_init_sd_web_ui = 448
    // 获取图片信息的fn_index
    const val fn_index_get_image_information = 369
    // 图片边缘检测的fn_index
    const val fn_index_canny = 55
    // 图片语义分割的fn_index
    const val fn_index_seg = 85
    // 图片深度估值的fn_index
    const val fn_index_leres = 115
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
    // 获取图片信息时负面词汇的题头
    const val negation_prompt_heading = "Negative prompt: "
    // 获取图片信息时步数的题头
    const val steps_heading = "Steps: "
    //获取图片信息时采样器的题头
    const val sampler_heading = ", Sampler: "
    //获取图片信息时图片相关程度的题头
    const val cfg_heading = ", CFG scale: "
    //获取图片信息时随机种子数的题头
    const val seed_heading = ", Seed: "
    //获取图片信息时图片大小的题头
    const val size_heading = ", Size: "
    //获取图片信息时模型哈希值的题头
    const val model_hash_heading = ", Model hash: "
    //获取图片信息时模型名称的题头
    const val model_heading = ", Model: "
    // 获取图片信息时跳过clip层数的题头
    const val clip_skip_heading = ", Clip skip: "
    // 获取图片信息时噪声种子增量的题头
    const val ensd_heading = ", ENSD: "
    // 获取图片信息时蒙版模糊的题头
    const val mask_blur_heading = ", Mask blur: "
    // 获取图片信息是重绘幅度的题头
    const val denoising_heading = ", Denoising strength: "
    //
    const val hires_upscale_heading = ", Hires upscale:"
    // 上传图片的Scheme头：
    const val image_base64_scheme = "data:image/png;base64,"
    // 图片后缀
    const val image_suffix = ".png"
    // 边缘检测预处理器
    const val canny_preprocessor = "canny"
    // 语义分割预处理器
    const val seg_preprocessor = "seg_ofade20k"
    // 深度估值预处理器
    const val leres_preprocessor = "depth_leres"

    // 改变模型后返回结果的type
    const val type_update = "update"

    val album_dir = "${Environment.DIRECTORY_PICTURES}/$appName"
    val album_content = "image/*"

    val takePhotoPermission = Manifest.permission.CAMERA

    val saveFilePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val samplers = listOf(
        "Euler a" ,
        "Euler" ,
        "LMS" ,
        "Heun" ,
        "DPM2" ,
        "DPM2 a" ,
        "DPM++ 2S a" ,
        "DPM++ 2M" ,
        "DPM++ SDE" ,
        "DPM fast" ,
        "DPM adaptive" ,
        "LMS Karras" ,
        "DPM2 Karras" ,
        "DPM2 a Karras" ,
        "DPM++ 2S a Karras" ,
        "DPM++ 2M Karras" ,
        "DPM++ SDE Karras" ,
        "DDIM" ,
        "PLMS"
    )
}