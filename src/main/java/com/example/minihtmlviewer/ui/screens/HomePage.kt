package com.example.minihtmlviewer.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.minihtmlviewer.R
import com.example.minihtmlviewer.util.getFileName
import com.example.minihtmlviewer.util.listSubDirectories
import deleteZipProject
import getZipProjects
import handleZipImport
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.TypeVariable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, context: Context){
    //AppBar颜色
    var appBarColor: Color = Color(0X1F,0X94,0XFF)
    //  维护 ZIP 项目列表
    var zipProjects = remember { mutableStateListOf<File>().apply { addAll(getZipProjects(context)) } }

    // 更新 ZIP 项目列表
    LaunchedEffect(Unit) {
        zipProjects.clear()
        zipProjects.addAll(getZipProjects(context)) // 初始化 ZIP 列表
    }

    //  ZIP 选择器
    val zipPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                val success = handleZipImport(context, it)
                if (success) {
                    // 重新获取 ZIP 列表
                    zipProjects.clear()
                    zipProjects.addAll(getZipProjects(context)) // 初始化 ZIP 列表
                }
            }
        }
    )

    Scaffold (
        topBar ={
            TopAppBar(
                title ={ Text(stringResource(id=R.string.app_name),color= Color.White)},
                colors= TopAppBarDefaults.smallTopAppBarColors(containerColor = appBarColor),//AppBar背景色
                actions = {
                    Row(
                        modifier = Modifier.clickable {
                            //点击事件
                            zipPickerLauncher.launch(arrayOf("application/zip")) // 选择 ZIP
                        }.padding(8.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint=Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("添加", color = Color.White)
                    };
                    Row(
                        modifier = Modifier.clickable {
                            // 重新获取 ZIP 列表
                            zipProjects.clear()
                            zipProjects.addAll(getZipProjects(context)) // 初始化 ZIP 列表
                        }.padding(8.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.Cached,
                            contentDescription = null,
                            tint=Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("刷新列表", color = Color.White)
                    }
                }
            )
        }
    ){
        paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 确保内容不会被 TopAppBar 覆盖
        ){
            FolderListScreen(zipProjects,navController)
        }

    }
}

@Composable
fun FolderListScreen(zipProjects: SnapshotStateList<File>, navController: NavController){
    val context= LocalContext.current
    var subDirectories by remember { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(Unit) {
        subDirectories= listSubDirectories(context)
    }
    //以下是UI部分
    if(zipProjects.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,

        ){
            Text("暂无项目",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }


    }else{
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(zipProjects){
                        folder ->
                    FolderItem(
                        icon = Icons.Filled.Language,
                        title = folder.name,
                        folderPath = folder,
                        onClick = {
                            navController.navigate("webview/${folder.name}")
                        },
                        onDelete = {
                            deleteZipProject(context, folder) //  触发删除文件夹
                            // 重新获取 ZIP 列表
                            zipProjects.remove(folder)
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderItem(
    icon: ImageVector= Icons.AutoMirrored.Filled.InsertDriveFile,//缺省图标
    title: String,
    folderPath:File,
    onClick:()->Unit,
    onDelete:()->Unit
){
    //读取 favorite.ico 或 favorite.png
    var folderIcon by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    // 控制 Dialog 显示
    var showDialog by remember { mutableStateOf(false) }
    //处理点击时的背景变化
    var isPressed by remember { mutableStateOf(false) }
    LaunchedEffect(folderPath) {
        val icoFile = File(folderPath, "favorite.ico")
        val pngFile = File(folderPath, "favorite.png")

        val iconFile = when {
            icoFile.exists() -> icoFile
            pngFile.exists() -> pngFile
            else -> null
        }

        iconFile?.let {
            val bitmap = BitmapFactory.decodeFile(it.absolutePath)
            folderIcon = bitmap?.asImageBitmap()
        }
    }

    Row(
        modifier = Modifier.fillMaxSize().padding(8.dp)
        .background(
            if(isPressed){
                Color.LightGray
            }else{
                Color.Transparent
            }
        ).combinedClickable(
            onClick = {
                onClick()
            },
            onLongClick = {
                showDialog=true //询问删除
            }
        )
    ){

        if (folderIcon != null) {
            Image(
                bitmap = folderIcon!!,
                contentDescription = "Folder Icon",
                modifier = Modifier.size(36.dp)
            )
        } else {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Filled.Language, // 默认图标
                contentDescription = "Default Folder Icon",
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 30.sp
        )
    }
    // ✅ 显示删除确认 Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("删除确认") },
            text = { Text("确定要删除 \"$title\" 吗？此操作无法撤销！") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete() // ✅ 触发删除操作
                    showDialog = false
                }) {
                    Text("删除", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}