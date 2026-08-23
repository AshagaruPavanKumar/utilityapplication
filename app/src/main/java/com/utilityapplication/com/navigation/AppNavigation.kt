package com.utilityapplication.com.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.utilityapplication.com.feature.emergency.pres.ui.scr.EmergencyScreen
import com.utilityapplication.com.feature.everday.pres.ui.scr.EverydayScreen
import com.utilityapplication.com.feature.finance.screen.AddExpenseScreen
import com.utilityapplication.com.feature.finance.screen.ExpenseInsightsScreen
import com.utilityapplication.com.feature.finance.screen.FinanceDashboardScreen
import com.utilityapplication.com.feature.finance.screen.ReportsScreen
import com.utilityapplication.com.feature.home.pres.ui.scr.UtilityKitHomeScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.QuickToolsScreen

@Composable
fun AppNavigation(startDestination: String = Routes.HOME) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in TopLevelRoutes

    LaunchedEffect(startDestination) {
        val target = if (startDestination in TopLevelRoutes) startDestination else Routes.HOME
        val current = navController.currentDestination?.route
        if (target == Routes.HOME && current == null) return@LaunchedEffect
        if (current != target) {
            navController.navigateToTab(target)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route -> navController.navigateToTab(route) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                UtilityKitHomeScreen(
                    onEverydayClick = { navController.navigateToTab(Routes.EVERYDAY) },
                    onFinanceClick = { navController.navigateToTab(Routes.FINANCE) },
                    onEmergencyClick = { navController.navigateToTab(Routes.EMERGENCY) },
                    onQuickToolsClick = { navController.navigateToTab(Routes.QUICK_TOOLS) }
                )
            }

            composable(Routes.EVERYDAY) {
                EverydayScreen(
                    onBackClick = { navController.navigateToTab(Routes.HOME) }
                )
            }

            composable(Routes.FINANCE) {
                FinanceDashboardScreen(navController = navController)
            }

            composable(Routes.EMERGENCY) {
                EmergencyScreen()
            }

            composable(Routes.QUICK_TOOLS) {
                QuickToolsScreen()
            }

            composable(Routes.ADD_EXPENSE) {
                AddExpenseScreen(
                    onBack = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            }

            composable(Routes.EXPENSE_INSIGHTS) {
                ExpenseInsightsScreen(navController = navController)
            }

            composable(Routes.REPORTS) {
                ReportsScreen(navController = navController)
            }
        }
    }
}
