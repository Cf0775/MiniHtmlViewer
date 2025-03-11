package com.example.minihtmlviewer.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

//获取私有路径
fun getAppFileDir(context: Context):String{
    return context.filesDir.absolutePath //返回路径:/data/data/{包名}/files/
}

//列出子目录
fun listSubDirectories(context: Context):List<String>{
    val filrDir=context.filesDir
    return filrDir.listFiles()
        ?.filter { it.isDirectory }
        ?.map { it.name } ?: emptyList()
}

//访问某目录下的某个子目录
fun getSubDirectories(context: Context,folderName:String): File {
    return File(context.filesDir,folderName)
}


// 获取用户所选文件的文件名
fun getFileName(context: android.content.Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        it.getString(nameIndex)
    }
}
