package com.example.minihtmlviewer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minihtmlviewer.ui.screens.HomePage
import com.example.minihtmlviewer.ui.screens.WebViewScreen
import com.example.minihtmlviewer.ui.screens.WelcomePage
import com.example.minihtmlviewer.ui.theme.MiniHtmlViewerTheme
import handleZipImport

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyAppNavHost(navController)
            //处理zip文件导入
            intent?.data?.let {
                uri ->
                    handleZipImport(this,uri)
            }
        }
    }

    @Composable
    fun MyAppNavHost(navController: NavHostController) {
        val context= LocalContext.current
        NavHost(navController = navController, startDestination = "welcome") {

            composable("welcome") { WelcomePage(navController,context) }
            composable("home_page") { HomePage(navController,context) }
            composable("webview/{folderName}") { backStackEntry ->
                WebViewScreen(navBackStackEntry = backStackEntry)
            }
        }
    }

}

