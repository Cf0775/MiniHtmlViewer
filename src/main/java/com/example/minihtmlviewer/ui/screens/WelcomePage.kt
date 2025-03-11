package com.example.minihtmlviewer.ui.screens

import RetroButton
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@Composable
fun WelcomePage(navController: NavHostController,context: Context){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        RetroButton("开始使用", onClick = {
            navController.navigate("home_page")
        },
        backgroundColor = Color.Gray,
        textColor = Color.Black
        )
    }
}