package com.policyboss.policybossproelite.utility

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object UTILITY {


      const val ErrorMessage : String = "Data Not Found.\nPlease try Again!!"

        @JvmStatic
        fun getFilePath(context: Context, contentUri: Uri): String? {
            try {
                val filePathColumn = arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.TITLE,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                )

                val returnCursor = contentUri.let { context.contentResolver.query(it, filePathColumn, null, null, null) }

                if (returnCursor != null) {

                    returnCursor.moveToFirst()
                    val nameIndex = returnCursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    val name = returnCursor.getString(nameIndex)
                    val file = File(context.cacheDir, name)
                    val inputStream = context.contentResolver.openInputStream(contentUri)
                    val outputStream = FileOutputStream(file)
                    var read: Int
                    val maxBufferSize = 1 * 1024 * 1024
                    val bytesAvailable = inputStream!!.available()

                    val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    val buffers = ByteArray(bufferSize)

                    while (inputStream.read(buffers).also { read = it } != -1) {
                        outputStream.write(buffers, 0, read)
                    }

                    inputStream.close()
                    outputStream.close()
                    return file.absolutePath
                }
                else
                {
                    Log.d("","returnCursor is null")
                    return null
                }
            }
            catch (e: Exception) {
                Log.d("","exception caught at getFilePath(): $e")
                return null
            }
        }

       @JvmStatic
       fun isFileLessThan5MB(file: File): Boolean {
        val maxFileSize = 5 * 1024 * 1024
        val l = file.length()
        val fileSize = l.toString()
        val finalFileSize = fileSize.toInt()
        return finalFileSize >= maxFileSize
    }

    @SuppressLint("HardwareIds")
     fun getDeviceDetai(context: Context): String {
        try {
        return "Brand: ${Build.BRAND} \n" +
                "DeviceName: ${Build.BRAND} - ${Build.MODEL} \n" +
                "DeviceID: ${
                    Settings.Secure.getString(
                        context.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                } \n" +
                "Model: ${Build.MODEL} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
        }catch (ex: Exception){
            return ""
        }
    }

    fun getDeviceDetail(context: Context) : DeviceDetailEntity {

      return  DeviceDetailEntity(

            Model = Build.MODEL ?: "",
            ID = Build.ID?: "",

            SDK =  ""+ Build.VERSION.SDK_INT,
            Manufacture = Build.MANUFACTURER?: "",
            Brand = Build.BRAND?: "",

            User = Build.USER ?: "",
            Type = Build.TYPE ?: "",
            Base = ""+ Build.VERSION_CODES.BASE,

            Incremental = Build.VERSION.INCREMENTAL?: "",
            Board = Build.BRAND ?: "",
            Host = Build.HOST ?: "",

            FingerPrint = Build.FINGERPRINT ?: "",
            VersionCode = Build.VERSION.RELEASE?: ""
        )
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        try {
            return  Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }catch (ex: Exception){
            return ""
        }

    }

    @SuppressLint("HardwareIds")
    fun getDeviceName(): String {
        try {
            return  "${Build.BRAND}-${Build.MODEL}"
        }catch (ex: Exception){
            return ""
        }

    }
    @SuppressLint("HardwareIds")
    fun getOS(): String {
        try {
            return  "Android:${Build.VERSION.RELEASE}"
        }catch (ex: Exception){
            return ""
        }

    }

}

