package com.example.aidraw.pool

class CachePool private constructor(
    var scale: Float =  2f,
    var width: Int = 512,
    var height: Int = 700,
    var steps: Int = 20, // no
    var adjustmentWidth: Int = 0,
    var adjustmentHeight: Int = 0,
    var sampler: String = ConstantPool.samplers[0].name, // no
    var denoisiong: Float = 0.7f, // no
    var hdRepairSwitch: Boolean = false,
    var faceRepairSwitch: Boolean = false // no
) {
    companion object {
        val instance: CachePool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CachePool() }
    }

}