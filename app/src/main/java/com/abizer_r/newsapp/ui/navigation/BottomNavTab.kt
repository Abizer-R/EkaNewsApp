package com.abizer_r.newsapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.abizer_r.newsapp.R

enum class BottomNavTab {
    HOME, SAVED
}

@Composable
fun BottomNavTab.getTabLabel(): String {
    return when (this) {
        BottomNavTab.HOME -> stringResource(R.string.home)
        BottomNavTab.SAVED -> stringResource(R.string.saved)
    }
}

@Composable
fun BottomNavTab.getTabIcon(): ImageVector {
    return when (this) {
        BottomNavTab.HOME -> Icons.Default.Home
        BottomNavTab.SAVED -> Icons.Default.Favorite
    }
}

@Composable
fun BottomBar(selectedTab: BottomNavTab, onTabSelected: (BottomNavTab) -> Unit) {
    NavigationBar {
        BottomNavTab.entries.forEach { navTab ->
            val tabLabel = navTab.getTabLabel()
            NavigationBarItem(
                selected = selectedTab == navTab,
                onClick = { onTabSelected(navTab) },
                label = { Text(text = tabLabel) },
                icon = { Icon(navTab.getTabIcon(), contentDescription = tabLabel) }
            )
        }
    }
}