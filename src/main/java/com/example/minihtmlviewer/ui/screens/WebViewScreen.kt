package com.example.minihtmlviewer.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavBackStackEntry
import java.io.File

@Composable
fun WebViewScreen(navBackStackEntry: NavBackStackEntry) {
    val context = LocalContext.current
    val folderName = navBackStackEntry.arguments?.getString("folderName") ?: return

    val projectDir = File(context.filesDir, folderName) // 直接在 files/ 目录里查找
    val indexHtml = File(projectDir, "index.html")

    if (indexHtml.exists()) {
        AndroidView(factory = { WebView(it).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true // 允许访问本地文件
            settings.allowFileAccessFromFileURLs = true // 允许 file:// 加载本地文件
            webViewClient = WebViewClient()
            loadUrl("file://${indexHtml.absolutePath}")
        } }, modifier = Modifier.fillMaxSize())
    } else {
        Text("找不到 index.html", modifier = Modifier.fillMaxSize())
    }
}