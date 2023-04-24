package com.example.aidraw.Bean

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

    // 开启高清修复
    fun openHDRepire(){
        fn_index = ConstantPool.fn_index_open_hd
        data = listOf(true).toTypedArray()
    }


    /**
     * 设置生成图像的配置
     * 开启高清修复后需要调用
     * @param openHdRepire 是否开启高清修复
     * @param width 宽度
     * @param height 高度
     * @param scale 方法倍率
     * @param adjustmentWidth 调整后的宽度
     * @param adjustmentHeight 调整后的高度
     *
     */
    fun setGenerateConfig(
        fn_index: Int,
        openHdRepire: Boolean = true,
        width: Int = 512,
        height: Int = 512,
        scale: Int = 2,
        adjustmentWidth: Int = 0,
        adjustmentHeight: Int = 0
    ){
        this.fn_index = fn_index
        data = listOf<Any>(
            openHdRepire,
            width,
            height,
            scale,
            adjustmentWidth,
            adjustmentHeight
        ).toTypedArray()
    }





    fun setPosition(position: String){
        data[1] = position
    }
    fun setNavigation(navigation: String){
        data[2] = navigation
    }
}