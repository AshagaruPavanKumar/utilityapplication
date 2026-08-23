package com.utilityapplication.com.feature.finance.screen


import androidx.compose.foundation.layout.*
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
import com.utilityapplication.com.feature.finance.pres.vm.ExpenseInsightsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseInsightsScreen(navController: NavController) {

    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FinanceRepository(database) }
    val viewModel: ExpenseInsightsViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ExpenseInsightsViewModel(repository) as T
            }
        }
    )

    val totalSpent by viewModel.totalSpent.collectAsStateWithLifecycle()
    val breakdown by viewModel.categoryBreakdown.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Insights", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("JUNE 2026", color = Color.White.copy(0.8f), fontSize = 14.sp)
                    Text("₹${totalSpent.toInt()}", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text("Total Spent", color = Color.White.copy(0.85f), fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("INSIGHTS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))

            breakdown.forEach { item ->
                InsightCard(
                    icon = when (item.category) {
                        "Food" -> Icons.Default.Restaurant
                        "Travel" -> Icons.Default.Flight
                        "Shopping" -> Icons.Default.ShoppingBag
                        else -> Icons.Default.Receipt
                    },
                    title = item.category,
                    amount = "₹${item.total.toInt()}",
                    percentage = "${((item.total / totalSpent) * 100).toInt()}%"
                )
            }
        }
    }
}

@Composable
fun InsightCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, amount: String, percentage: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)
                Text(amount, color = Color.Gray, fontSize = 13.sp)
            }
            Text(percentage, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        }
    }
}