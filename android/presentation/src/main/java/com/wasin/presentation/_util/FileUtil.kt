package com.wasin.presentation._util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.ByteStreams
import java.io.File
import java.io.FileOutputStream

object FileUtil {
    fun fileFromContentUri(uri: Uri, context: Context): File {

        val fileExtension = getFileExtension(context, uri)
        val fileName = "wasin_file" + if (fileExtension != null) ".$fileExtension" else ""

        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.let { ByteStreams.copy(inputStream, oStream) }
            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

}
