package com.example.aidraw.pool

class CachePool private constructor(
    var scale: Float =  2f,
    var width: Int = 512,
    var height: Int = 700,
    var steps: Int = 20, // no
    var adjustmentWidth: Int = 0,
    var adjustmentHeight: Int = 0,
    var sampler: String = ConstantPool.samplers[0], // no
    var denoisiong: Float = 0.7f, // no
    var hdRepairSwitch: Boolean = false,
    var model: String = "",
    var supportModels: List<String> = listOf(),
    var cannySwith: Boolean = false,
    var segSwith: Boolean = false,
    var leresSwith: Boolean = false,
    var faceRepairSwitch: Boolean = false, // no
) {
    companion object {
        val instance: CachePool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CachePool() }
    }

}