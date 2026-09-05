package com.utilityapplication.com.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val TopLevelRoutes = setOf(
    Routes.HOME,
    Routes.EVERYDAY,
    Routes.FINANCE,
    Routes.EMERGENCY,
    Routes.QUICK_TOOLS
)

val bottomNavItems = listOf(
    BottomNavItem(Routes.HOME, "Home", Icons.Default.Home),
    BottomNavItem(Routes.EVERYDAY, "Everyday", Icons.Default.GridView),
    BottomNavItem(Routes.FINANCE, "Finance", Icons.AutoMirrored.Filled.ShowChart),
    BottomNavItem(Routes.EMERGENCY, "Emergency", Icons.Default.Warning),
    BottomNavItem(Routes.QUICK_TOOLS, "Quick Tools", Icons.Default.Apps)
)

fun NavController.navigateToTab(route: String) {
    if (currentDestination?.route == route) return
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onTabSelected(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) }
            )
        }
    }
}
