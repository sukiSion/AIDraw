package com.example.aidraw.util

import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.aidraw.pool.ConstantPool
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream

/**
 * 保存Bitmap到相册的Pictures文件夹
 *
 * @param context 上下文
 * @param fileName 文件名。 需要携带后缀
 * @param relativePath 相对于Pictures的路径
 * @param quality 质量
 */
object SavePhotoUtil{
    fun saveToAlbum(
        bitmap: Bitmap ,
        context: Context,
        fileName: String,
        quality : Int = 0,
        relativePath: String? = null,
        onError: () -> Unit = {},
        onSuccess: (Uri) -> Unit = {}
    ): Uri? {
        val resolver = context.contentResolver
        val outputFile = OutputFileTaker()
        // 插入图片信息
        val imageUri = insertMediaImage(resolver , fileName, relativePath, outputFile)
        if (imageUri == null) {
            onError()
            return null
        }

        // 通过Uri打开输出流
        (outputStream(imageUri , resolver) ?: return null).use {
            val format = getBitmapFormat(fileName)
            // 保存图片
            bitmap.compress(format, quality, it)
            // 更新 IS_PENDING 状态
            finishPending(imageUri , context, resolver, outputFile.file)
        }
        onSuccess(imageUri)
        return imageUri
    }

    private class OutputFileTaker(var file: File? = null)

    /**
     * 插入图片到媒体库
     */
    private fun insertMediaImage(
        contentResolver: ContentResolver,
        fileName: String,
        relativePath: String?,
        outputFileTaker: OutputFileTaker? = null
    ): Uri? {
        // 图片信息
        val imageValues = ContentValues().apply {
            val mimeType = getMimeType(fileName)
            if (mimeType != null) {
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            }
            // 插入时间
            val date = System.currentTimeMillis() / 1000
            put(MediaStore.Images.Media.DATE_ADDED, date)
            put(MediaStore.Images.Media.DATE_MODIFIED, date)
        }
        // 保存的位置
        val collection: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val path = if (relativePath != null) "${ConstantPool.album_dir}/${relativePath}" else ConstantPool.album_dir
            imageValues.apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            // 高版本不用查重直接插入，会自动重命名
        } else {
            // 老版本
            val pictures = Environment.getExternalStoragePublicDirectory(ConstantPool.album_dir)
            val saveDir = if (relativePath != null) File(pictures, relativePath) else pictures

            if (!saveDir.exists() && !saveDir.mkdirs()) {
                return null
            }

            // 文件路径查重，重复的话在文件名后拼接数字
            var imageFile = File(saveDir, fileName)
            val fileNameWithoutExtension = imageFile.nameWithoutExtension
            val fileExtension = imageFile.extension

            // 查询文件是否已经存在
            var queryUri = queryMediaImage28(contentResolver , imageFile.absolutePath)
            var suffix = 1
            while (queryUri != null) {
                // 存在的话重命名，路径后面拼接 fileNameWithoutExtension(数字).png
                val newName = fileNameWithoutExtension + "(${suffix++})." + fileExtension
                imageFile = File(saveDir, newName)
                queryUri = this.queryMediaImage28(contentResolver , imageFile.absolutePath)
            }

            imageValues.apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
                // 保存路径
                val imagePath = imageFile.absolutePath
                put(MediaStore.Images.Media.DATA, imagePath)
            }
            outputFileTaker?.file = imageFile// 回传文件路径，用于设置文件大小
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        // 插入图片信息
        return contentResolver.insert(collection, imageValues)
    }

    /**
     * Android Q以下版本，查询媒体库中当前路径是否存在
     * @return Uri 返回null时说明不存在，可以进行图片插入逻辑
     */
    private fun queryMediaImage28(contentResolver: ContentResolver , imagePath: String): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return null

        val imageFile = File(imagePath)
        if (imageFile.canRead() && imageFile.exists()) {
            // 文件已存在，返回一个file://xxx的uri
            // 这个逻辑也可以不要，但是为了减少媒体库查询次数，可以直接判断文件是否存在
            return Uri.fromFile(imageFile)
        }
        // 保存的位置
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // 查询是否已经存在相同图片
        val query = contentResolver.query(
            collection,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
            "${MediaStore.Images.Media.DATA} == ?",
            arrayOf(imagePath), null
        )
        query?.use {
            while (it.moveToNext()) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = it.getLong(idColumn)
                val existsUri = ContentUris.withAppendedId(collection, id)
                return existsUri
            }
        }
        return null
    }

    private fun finishPending(
        uri: Uri,
        context: Context,
        resolver: ContentResolver,
        outputFile: File?
    ) {
        val imageValues = ContentValues()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (outputFile != null) {
                // Android 10 以下需要更新文件大小字段，否则部分设备的图库里照片大小显示为0
                imageValues.put(MediaStore.Images.Media.SIZE, outputFile.length())
            }
            resolver.update(uri, imageValues, null, null)
            // 通知媒体库更新，部分设备不更新 图库看不到 ？？？
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
            context.sendBroadcast(intent)
        } else {
            // Android Q添加了IS_PENDING状态，为0时其他应用才可见
            imageValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, imageValues, null, null)
        }
    }



    private fun outputStream(uri: Uri , resolver: ContentResolver): OutputStream? {
        return try {
            resolver.openOutputStream(uri)
        } catch (e: FileNotFoundException) {
            null
        }
    }
    // 获取不同的图片类型
    private fun getMimeType(name: String): String? {
        val fileName = name.lowercase()
        return when {
            fileName.endsWith(".png") -> "image/png"
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
            fileName.endsWith(".webp") -> "image/webp"
            fileName.endsWith(".gif") -> "image/gif"
            else -> null
        }
    }

    // string转图片
    private fun getBitmapFormat(name: String): Bitmap.CompressFormat {
        val fileName = name.lowercase()
        return when {
            fileName.endsWith(".png") -> Bitmap.CompressFormat.PNG
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> Bitmap.CompressFormat.JPEG
            fileName.endsWith(".webp") -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.PNG
        }
    }
}










