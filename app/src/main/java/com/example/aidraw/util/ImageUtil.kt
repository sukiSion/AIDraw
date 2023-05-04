package com.example.aidraw.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.example.aidraw.pool.ConstantPool
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object ImageUtil {

    fun getFilePathFromURI(context: Context, contentUri: Uri?): String? {
        val rootDataDir = context.getExternalFilesDir(null)
        //        MyApplication.getMyContext().getExternalFilesDir(null).getPath();
        val fileName = getFileName(contentUri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(rootDataDir.toString() + File.separator + fileName)
            copyFile(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copyFile(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @Throws(java.lang.Exception::class, IOException::class)
    fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val BUFFER_SIZE = 1024 * 2
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
            }
            try {
                `in`.close()
            } catch (e: IOException) {
            }
        }
        return count
    }


    /**
     * 通过uri  获取文件路径
     * @param context
     * @param imageUri
     * @return
     */
    fun getFileAbsolutePath(context: Context , imageUri: Uri):String?{

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && DocumentsContract.isDocumentUri(
                context,
                imageUri
            )
        ) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            }
        } // MediaStore (and general)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return uriToFileApiQ(context, imageUri)
        } else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) {
                imageUri.lastPathSegment
            } else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }
        return null
    }

    // 此方法 只能用于4.4以下的版本]
    fun getRealFilePath(context: Context , uri: Uri):String?{
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Android 10 以上适配 另一种写法
     * @param context
     * @param uri
     * @return
     */
    private fun getFileFromContentUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val filePath: String
        val filePathColumn =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            uri, filePathColumn, null,
            null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            try {
                filePath = cursor.getString(cursor.getColumnIndexOrThrow(filePathColumn[0]))
                return filePath
            } catch (e: Exception) {
            } finally {
                cursor.close()
            }
        }
        return ""
    }

    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun uriToFileApiQ(context: Context, uri: Uri): String? {
        var file: File? = null
        //android10以上转换
        if (uri.scheme == ContentResolver.SCHEME_FILE) {
            file = File(uri.path)
        } else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            //把文件复制到沙盒目录
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val displayName =
                    cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    val cache = File(
                        context.externalCacheDir!!.absolutePath,
                        Math.round((Math.random() + 1) * 1000).toString() + displayName
                    )
                    val fos = FileOutputStream(cache)
                    FileUtils.copy(inputStream!!, fos)
                    file = cache
                    fos.close()
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file?.absolutePath
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    fun imageToBase64(path: String?): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var inputStream: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            inputStream = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(inputStream.available())
            //写入数组
            inputStream.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.NO_CLOSE)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    /**
     * 将Base64编码转换为图片
     * @param base64Str
     * @param path
     * @return true
     */
    fun base64ToFile(base64Str: String?, path: String?): Boolean {
        val data = Base64.decode(base64Str, Base64.NO_WRAP)
        for (i in data.indices) {
            if (data[i] < 0) {
                //调整异常数据
                data[i] = (data[i] + 256).toByte()
            }
        }
        var os: OutputStream? = null
        return try {
            os = FileOutputStream(path)
            os.write(data)
            os.flush()
            os.close()
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun getCacheImagePath(context: Context):String{
        val storageFile: File? =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                context.externalCacheDir
            } else {
                context.cacheDir
            }
        val photoFile = File.createTempFile("tmp_image_file", ConstantPool.image_suffix, storageFile).apply {
            createNewFile()
            delete()
        }
        return photoFile.absolutePath
    }

    fun getCameraUri(
        context: Context
    ):Pair<Uri? , String?>{
        val storageFile: File? =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                context.externalCacheDir
            } else {
                context.cacheDir
            }

        val photoFile = File.createTempFile("tmp_image_file", ConstantPool.image_suffix, storageFile).apply {
            createNewFile()
            delete()
        }
        val fileProviderUri = FileProvider.getUriForFile(context, "com.example.aidraw.fileProvider", photoFile)

        return fileProviderUri to photoFile.absolutePath
    }
}