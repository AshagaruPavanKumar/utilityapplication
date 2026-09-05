package com.utilityapplication.com.navigation

import androidx.compose.foundation.layout.Column
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
import com.utilityapplication.com.ads.BannerAdSlot
import com.utilityapplication.com.feature.emergency.pres.ui.scr.EmergencyScreen
import com.utilityapplication.com.feature.emergency.pres.ui.scr.MedicalCardScreen
import com.utilityapplication.com.feature.emergency.pres.ui.scr.SosFlashScreen
import com.utilityapplication.com.feature.everday.pres.ui.scr.EverydayScreen
import com.utilityapplication.com.feature.finance.screen.AddExpenseScreen
import com.utilityapplication.com.feature.finance.screen.BudgetGoalsScreen
import com.utilityapplication.com.feature.finance.screen.ExpenseInsightsScreen
import com.utilityapplication.com.feature.finance.screen.FinanceDashboardScreen
import com.utilityapplication.com.feature.finance.screen.ReportsScreen
import com.utilityapplication.com.feature.home.pres.ui.scr.UtilityKitHomeScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.BmiCalculatorScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.DateDifferenceScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.DiscountCalculatorScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.EmiCalculatorScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.GstCalculatorScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.PercentageCalculatorScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.QuickToolsScreen
import com.utilityapplication.com.feature.quicktools.pres.ui.scr.TipCalculatorScreen
import com.utilityapplication.com.feature.settings.SettingsScreen

private val DirectRoutes = TopLevelRoutes + setOf(Routes.SETTINGS)

@Composable
fun AppNavigation(startDestination: String = Routes.HOME) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in TopLevelRoutes
    val showBannerAd = currentRoute != Routes.EMERGENCY &&
        currentRoute != Routes.ADD_EXPENSE &&
        currentRoute != Routes.SOS_FLASH &&
        currentRoute != Routes.MEDICAL_CARD

    LaunchedEffect(startDestination) {
        val target = if (startDestination in DirectRoutes) startDestination else Routes.HOME
        val current = navController.currentDestination?.route
        if (target == Routes.HOME && current == null) return@LaunchedEffect
        if (current != target) {
            if (target in TopLevelRoutes) navController.navigateToTab(target)
            else navController.navigate(target)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Column {
                if (showBannerAd) {
                    BannerAdSlot()
                }
                if (showBottomBar) {
                    AppBottomBar(
                        currentRoute = currentRoute,
                        onTabSelected = { route -> navController.navigateToTab(route) }
                    )
                }
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
                    onQuickToolsClick = { navController.navigateToTab(Routes.QUICK_TOOLS) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.EVERYDAY) {
                EverydayScreen(
                    onBackClick = { navController.navigateToTab(Routes.HOME) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.FINANCE) {
                FinanceDashboardScreen(navController = navController)
            }
            composable(Routes.EMERGENCY) {
                EmergencyScreen(
                    onMedicalCard = { navController.navigate(Routes.MEDICAL_CARD) },
                    onScreenFlash = { navController.navigate(Routes.SOS_FLASH) }
                )
            }
            composable(Routes.QUICK_TOOLS) {
                QuickToolsScreen(
                    onOpenTool = { navController.navigate(it) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) }
                )
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
            composable(Routes.BUDGET_GOALS) {
                BudgetGoalsScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.MEDICAL_CARD) {
                MedicalCardScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.SOS_FLASH) {
                SosFlashScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.BMI) { BmiCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.PERCENTAGE) { PercentageCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.TIP) { TipCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.DISCOUNT) { DiscountCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.EMI) { EmiCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.GST) { GstCalculatorScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.DATE_DIFF) { DateDifferenceScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
