package com.utilityapplication.com.feature.finance.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utilityapplication.com.core.prefs.AppPreferences
import com.utilityapplication.com.feature.finance.data.AppDatabase
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import com.utilityapplication.com.feature.finance.pres.vm.FinanceDashboardViewModel
import com.utilityapplication.com.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceDashboardScreen(
    navController: NavController,
    onSettingsClick: () -> Unit = { navController.navigate(Routes.SETTINGS) }
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FinanceRepository(database) }
    val viewModel: FinanceDashboardViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FinanceDashboardViewModel(repository) as T
            }
        }
    )
    val prefs = remember { AppPreferences.get(context) }
    val budgets = remember { prefs.getBudgetGoals() }

    val totalSpent by viewModel.totalSpentThisMonth.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()
    val todayTotal by viewModel.todayTotal.collectAsStateWithLifecycle()
    val todayCount by viewModel.todayCount.collectAsStateWithLifecycle()
    val weekTotal by viewModel.weekTotal.collectAsStateWithLifecycle()
    val weekCount by viewModel.weekCount.collectAsStateWithLifecycle()
    val monthlyBreakdown by viewModel.monthlyBreakdown.collectAsStateWithLifecycle()
    val dateFormat = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_EXPENSE) },
                containerColor = MaterialTheme.colorScheme.primary
            ) { Icon(Icons.Default.Add, contentDescription = "Add Expense") }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("TODAY", color = Color.White.copy(0.8f), fontSize = 14.sp)
                        Text("₹${"%.2f".format(todayTotal)}", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Text("$todayCount transaction${if (todayCount == 1) "" else "s"} today", color = Color.White.copy(0.9f))
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("This Week", "₹${weekTotal.toInt()}", "$weekCount txns", Modifier.weight(1f))
                    StatCard("This Month", "₹${totalSpent.toInt()}", "spent", Modifier.weight(1f))
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
            item { Text("MONTHLY BREAKDOWN", fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }
            item { Spacer(Modifier.height(8.dp)) }
            if (monthlyBreakdown.isEmpty()) {
                item { Text("No spending this month yet.", color = Color.Gray) }
            } else {
                items(monthlyBreakdown.size) { index ->
                    val item = monthlyBreakdown[index]
                    val share = if (totalSpent == 0.0) 0f else (item.total / totalSpent).toFloat()
                    val goal = budgets[item.category]
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(item.category, fontWeight = FontWeight.Medium)
                            Text("₹${item.total.toInt()}", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { share.coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp))
                        )
                        if (goal != null && goal > 0) {
                            val ratio = item.total / goal
                            val warning = if (ratio >= 1) "Over budget" else if (ratio >= 0.8) "Nearing limit" else null
                            if (warning != null) {
                                Text("$warning · ₹${item.total.toInt()} / ₹${goal.toInt()}", color = Color(0xFFD32F2F), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
            item { Text("RECENT TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }
            item { Spacer(Modifier.height(8.dp)) }
            items(recentTransactions.take(8).size) { index ->
                val transaction = recentTransactions[index]
                TransactionItem(
                    title = transaction.category,
                    amount = if (transaction.isExpense) "-₹${transaction.amount.toInt()}" else "+₹${transaction.amount.toInt()}",
                    date = dateFormat.format(Date(transaction.date))
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { navController.navigate(Routes.EXPENSE_INSIGHTS) }) {
                        Text("Insights")
                    }
                    TextButton(onClick = { navController.navigate(Routes.REPORTS) }) {
                        Text("Reports")
                    }
                    TextButton(onClick = { navController.navigate(Routes.BUDGET_GOALS) }) {
                        Text("Budgets")
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
    }
}

@Composable
fun StatCard(title: String, amount: String, subtitle: String = "", modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Text(amount, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (subtitle.isNotBlank()) Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun TransactionItem(title: String, amount: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.Gray)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }
        Text(
            amount,
            fontWeight = FontWeight.SemiBold,
            color = if (amount.startsWith("-")) Color.Red else Color(0xFF4CAF50)
        )
    }
}
