package com.example.skai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.skai.ui.navigation.SkaiNavigation

@Composable
fun SkaiApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var isInitialized by remember { mutableStateOf(false) }
    

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            DataManager.initialize()
            isInitialized = true
        }
    }
    
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isInitialized) {
            SkaiNavigation(navController = navController)
        } else {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6A5ACD))
            }
        }
    }
}
