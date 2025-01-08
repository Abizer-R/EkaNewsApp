package com.abizer_r.newsapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.abizer_r.newsapp.R
import com.abizer_r.newsapp.ui.NewsApp
import com.abizer_r.newsapp.ui.home.HomeScreen
import com.abizer_r.newsapp.ui.saved.SavedScreen
import com.abizer_r.newsapp.ui.theme.NewsAppTheme

@Composable
fun BottomNavBar(
    navController: NavController,
    bottomTabItems: List<BottomNavTabs>,
) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    NavigationBar {
        bottomTabItems.forEach { navTab ->
            val isSelected = currentRoute == navTab.route
            val tabLabel = stringResource(navTab.titleResId)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(navTab.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                label = { Text(text = tabLabel) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            navTab.selectedIcon
                        } else navTab.unSelectedIcon,
                        contentDescription = tabLabel
                    )
                }
            )
        }
    }
}

@Composable
fun NewsNavHostContainer(navController: NavHostController, paddingValue: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = BottomNavTabs.Home.route,
        modifier = Modifier.padding(paddingValue).fillMaxSize()
    ) {
        composable(BottomNavTabs.Home.route) {
            HomeScreen(
                goToSavedScreen = {
                    navController.navigate(BottomNavTabs.Saved.route) {
                        popUpTo(BottomNavTabs.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(BottomNavTabs.Saved.route) {
            SavedScreen()
        }
    }
}

sealed class BottomNavTabs(
    val route: String,
    @StringRes val titleResId: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {
    data object Home: BottomNavTabs(
        route = "Home",
        titleResId = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home
    )
    data object Saved: BottomNavTabs(
        route = "Saved",
        titleResId = R.string.saved,
        selectedIcon = Icons.Default.Bookmark,
        unSelectedIcon = Icons.Default.BookmarkBorder
    )
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    NewsAppTheme {
        NewsApp()
    }
}