package com.example.aidraw.Bean

import com.example.aidraw.pool.CachePool
import com.example.aidraw.pool.ConstantPool

data class RequestBean(
    var data: Array<Any?> = listOf<Any?>(
        "task(yxq1z5yak9zck5r)",
        "",
        "",
        listOf<Any>(),
        20,
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
        false,
        0.7,
        2,
        "Latent",
        0,
        0,
        0,
        listOf<Any>(),
        "None",
        null,
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
        null,
        false,
        50,
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

    // 设置获取图片信息的data
    fun setGetImageInformation(imageBase64: String){
        fn_index  = ConstantPool.fn_index_get_image_information
        data = listOf(imageBase64).toTypedArray()
    }


    // 设置clip反推提示词的data
    fun setClipReversePrompt(imageBase64: String){
        fn_index = ConstantPool.fn_index_clip_reverse_prompt
        data = listOf<Any?>(
            0,
            "",
            "",
            imageBase64,
            null,
            null,
            null,
            null
        ).toTypedArray()
    }

    // 设置deepbooru反推提示词的data
    fun setDeepBooruReversePrompt(imageBase64: String){
        fn_index = ConstantPool.fn_index_deepbooru_reverse_prompt
        data = listOf<Any?>(
            0,
            "",
            "",
            imageBase64,
            null,
            null,
            null,
            null
        ).toTypedArray()
    }

    // 设置文生图的data
    fun setText2ImageData(position: String , negation: String){
        data = listOf(
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
            null,
            null,
            null,
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
            null,
            false,
            null,
            false,
            null,
            false,
            50,
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

    // 设置图生图的data
    fun setImage2ImageData(position: String , negation: String , imageBase64: String){
        fn_index = ConstantPool.fn_index_image2image
        data = listOf<Any?>(
            "task(vgcf7twod031k6r)",
            0,
            position,
            negation,
            listOf<Any?>(),
            imageBase64,
            null,
            null,
            null,
            null,
            null,
            null,
            CachePool.instance.steps,
            CachePool.instance.sampler,
            4, // 蒙版模糊
            0,
            "original",
            false,
            false,
            1,
            1,
            7,
            1.5,
            CachePool.instance.denoisiong,
            -1,
            -1,
            0,
            0,
            0,
            false,
            CachePool.instance.height, // 高度
            CachePool.instance.width, // 宽度
            "Crop and resize",
            "Whole picture",
            32,
            "Inpaint masked",
            "",
            "",
            "",
            listOf<Any?>(),
            "None",
            null,
            null,
            null,
            "<ul>\n<li><code>CFG Scale</code> should be 2 or lower.</li>\n</ul>\n",
            true,
            true,
            "",
            "",
            true,
            50,
            true,
            1,
            0,
            false,
            4,
            1,
            "None",
            "<p style=\"margin-bottom:0.75em\">Recommended settings: Sampling Steps: 80-100, Sampler: Euler a, Denoising strength: 0.8</p>",
            128,
            8,
            listOf(
                "left",
                "right",
                "up",
                "down"
            ),
            1,
            0.05,
            128,
            4,
            "fill",
            listOf(
                "left",
                "right",
                "up",
                "down"
            ),
            false,
            false,
            "positive",
            "comma",
            0,
            false,
            false,
            "",
            "<p style=\"margin-bottom:0.75em\">Will upscale the image by the selected scale factor; use width and height sliders to set tile size</p>",
            64,
            "None",
            2,
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
            null,
            false,
            null,
            false,
            null,
            false,
            50,
            listOf<Any?>(),
            "",
            "",
            "",
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

    // 设置切换模型的data
    fun setChangeModelData(model : String){
        fn_index = ConstantPool.fn_index_model
        data = listOf(
            model
        ).toTypedArray()
    }

    fun setCannyAndSegData(imageBase64: String, preprocessor:String){
        data = listOf<Any?>(
            PreprocessingBean(imageBase64 , imageBase64),
            preprocessor,
            512,
            100,
            200,
            512,
            512,
            false,
            "Crop and Resize"
        ).toTypedArray()
    }

    fun setLeResData(imageBase64: String, preprocessor:String){
        data = listOf<Any?>(
            PreprocessingBean(imageBase64 , imageBase64),
            preprocessor,
            512,
            0,
            0,
            512,
            512,
            false,
            "Crop and Resize"
        ).toTypedArray()
    }
}