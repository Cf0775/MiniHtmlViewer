import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun handleZipImport(context: Context, zipUri: Uri): Boolean {
    val rootDir = context.filesDir //  直接解压到 files/

    val zipName = getCleanFileName(zipUri) ?: return false
    var targetDir = File(rootDir, zipName)

    //  确保名称不重复
    var counter = 1
    while (targetDir.exists()) {
        targetDir = File(rootDir, "${zipName}_$counter")
        counter++
    }

    //  解压 ZIP（必须在根目录有 index.html，否则不导入）
    if (!unzipToTarget(context, zipUri, targetDir)) {
        Toast.makeText(context, "ZIP 无效，根路径必须包含 index.html", Toast.LENGTH_SHORT).show()
        return false
    }

    Toast.makeText(context, "导入成功: ${targetDir.name}", Toast.LENGTH_SHORT).show()
    return true
}

//  解压 ZIP 到指定目录，并检查 index.html
private fun unzipToTarget(context: Context, zipUri: Uri, outputDir: File): Boolean {
    var hasIndexHtml = false
    outputDir.mkdirs() //  确保目录存在

    context.contentResolver.openInputStream(zipUri)?.use { inputStream ->
        ZipInputStream(inputStream).use { zipStream ->
            var entry: ZipEntry?
            while (zipStream.nextEntry.also { entry = it } != null) {
                entry?.let { zipEntry ->
                    val outputFile = File(outputDir, zipEntry.name)

                    if (zipEntry.isDirectory) {
                        outputFile.mkdirs()
                    } else {
                        outputFile.parentFile?.mkdirs()
                        outputFile.outputStream().use { output ->
                            zipStream.copyTo(output)
                        }

                        //  确保 index.html 在根目录
                        if (zipEntry.name == "index.html" && zipEntry.name.split("/").size == 1) {
                            hasIndexHtml = true
                        }else{
                            hasIndexHtml = false
                        }
                    }
                }
            }
        }
    }
    return hasIndexHtml
}

//  获取 ZIP 解压后的文件夹列表（不显示 zip_projects/）
fun getZipProjects(context: Context): List<File> {
    return context.filesDir.listFiles()?.filter { it.isDirectory } ?: emptyList()
}

//解析ZIP文件名
fun getCleanFileName(uri: Uri):String?{
    return uri.lastPathSegment?.substringAfterLast(":")?.removeSuffix(".zip")
}

fun deleteZipProject(context: Context, folder: File) {
    if (folder.exists()) {
        folder.deleteRecursively()
        Toast.makeText(context, "已删除 ${folder.name}", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "文件夹不存在", Toast.LENGTH_SHORT).show()
    }
}
