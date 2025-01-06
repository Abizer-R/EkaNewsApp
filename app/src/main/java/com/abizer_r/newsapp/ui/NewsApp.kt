package com.abizer_r.newsapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.abizer_r.newsapp.ui.navigation.BottomNavBar
import com.abizer_r.newsapp.ui.navigation.BottomNavTabs
import com.abizer_r.newsapp.ui.navigation.NewsNavHostContainer
import com.abizer_r.newsapp.ui.theme.NewsAppTheme


@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val tabs = listOf(BottomNavTabs.Home, BottomNavTabs.Saved)
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, tabs)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            NewsNavHostContainer(navController, paddingValues)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    NewsAppTheme {
        NewsApp()
    }
}
