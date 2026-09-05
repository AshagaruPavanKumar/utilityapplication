package com.utilityapplication.com.feature.finance.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val lastMonth by viewModel.lastMonthSpent.collectAsStateWithLifecycle()
    val breakdown by viewModel.categoryBreakdown.collectAsStateWithLifecycle()
    val mom by viewModel.monthOverMonthPercent.collectAsStateWithLifecycle()
    val monthLabel = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()) }
    val topCategory = breakdown.maxByOrNull { it.total }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Insights", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(monthLabel.uppercase(), color = Color.White.copy(0.8f), fontSize = 14.sp)
                    Text("₹${totalSpent.toInt()}", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    val momLabel = if (mom >= 0) "+${"%.1f".format(mom)}% vs last month" else "${"%.1f".format(mom)}% vs last month"
                    Text(momLabel, color = Color.White.copy(0.9f), fontSize = 14.sp)
                    Text("Last month ₹${lastMonth.toInt()}", color = Color.White.copy(0.75f), fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            if (topCategory != null) {
                Text("Top category: ${topCategory.category}", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
            }
            Text("CATEGORY SHARE", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(12.dp))
            breakdown.forEach { item ->
                InsightCard(
                    icon = categoryIcon(item.category),
                    title = item.category,
                    amount = "₹${item.total.toInt()}",
                    percentage = if (totalSpent == 0.0) "0%" else "${((item.total / totalSpent) * 100).toInt()}%"
                )
            }
        }
    }
}

@Composable
fun InsightCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    amount: String,
    percentage: String
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)
                Text(amount, color = Color.Gray, fontSize = 13.sp)
            }
            Text(percentage, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
