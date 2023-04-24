package com.example.aidraw.pool

class CachePool private constructor(
    var scale: Int =  2,
    var width: Int = 512,
    var height: Int = 512,
) {

    companion object {
        val instance: CachePool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CachePool() }
    }


}