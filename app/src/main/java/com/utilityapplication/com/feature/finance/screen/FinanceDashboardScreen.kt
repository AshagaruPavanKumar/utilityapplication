package com.utilityapplication.com.feature.finance.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utilityapplication.com.feature.finance.data.AppDatabase
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import com.utilityapplication.com.feature.finance.pres.vm.FinanceDashboardViewModel
import com.utilityapplication.com.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceDashboardScreen(navController: NavController) {

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

    val totalSpent by viewModel.totalSpentThisMonth.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.recentTransactions.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_EXPENSE) },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("SPENT THIS MONTH", color = Color.White.copy(0.8f), fontSize = 14.sp)
                        Text("₹${totalSpent.toInt()}", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("This Week", "₹4,800", Modifier.weight(1f))
                    StatCard("This Month", "₹${totalSpent.toInt()}", Modifier.weight(1f))
                }
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                Text("RECENT TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            item { Spacer(Modifier.height(8.dp)) }

            items(recentTransactions.size) { index ->
                val transaction = recentTransactions[index]
                TransactionItem(
                    title = transaction.category,
                    amount = if (transaction.isExpense) "-₹${transaction.amount.toInt()}" else "+₹${transaction.amount.toInt()}",
                    date = "Today"
                )
            }

            item {
                TextButton(onClick = { navController.navigate(Routes.REPORTS) }) {
                    Text("See All", color = Color(0xFF1976D2))
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, amount: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 13.sp)
            Text(amount, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TransactionItem(title: String, amount: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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