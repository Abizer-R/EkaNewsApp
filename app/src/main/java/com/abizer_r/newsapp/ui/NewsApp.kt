package com.abizer_r.newsapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abizer_r.newsapp.ui.home.HomeScreen
import com.abizer_r.newsapp.ui.navigation.BottomBar
import com.abizer_r.newsapp.ui.navigation.BottomNavTab
import com.abizer_r.newsapp.ui.saved.SavedScreen
import com.abizer_r.newsapp.ui.theme.NewsAppTheme


@Composable
fun NewsApp() {
    var selectedTab: BottomNavTab by remember { mutableStateOf(BottomNavTab.HOME) }
    Scaffold(
        bottomBar = {
            BottomBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                BottomNavTab.HOME -> HomeScreen()
                BottomNavTab.SAVED -> SavedScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        NewsApp()
    }
}
