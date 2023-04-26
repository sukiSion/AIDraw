package com.example.aidraw.pool

class CachePool private constructor(
    var scale: Float =  2f,
    var width: Int = 512,
    var height: Int = 512,
    var steps: Int = 20,
    var adjustmentWidth: Int = 0,
    var adjustmentHeight: Int = 0,
    var simpler: String = ConstantPool.samplers[0].name,
    val denoisiong: Float = 0.7f
) {

    companion object {
        val instance: CachePool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CachePool() }
    }


}