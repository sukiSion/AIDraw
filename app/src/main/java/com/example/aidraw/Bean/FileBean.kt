package com.example.aidraw.Bean

import com.example.aidraw.pool.ConstantPool
import java.text.SimpleDateFormat
import java.util.Date

// data和name用于保存在云端
data class FileBean(
    var data: String = "",
    val is_file: Boolean = true,
    var name: String = ""
){
    fun setFileName(fileName:String){
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = simpleDateFormat.format(Date(System.currentTimeMillis()))
        name = "/root/autodl-tmp/stable-diffusion-webui/outputs/txt2img-images/${today}/${fileName}${ConstantPool.image_suffix}"
        data = "file=${name}"
    }


}