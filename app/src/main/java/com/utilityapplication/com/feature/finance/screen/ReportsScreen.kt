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
import com.utilityapplication.com.feature.finance.pres.vm.ReportsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {

    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FinanceRepository(database) }
    val viewModel: ReportsViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ReportsViewModel(repository) as T
            }
        }
    )

    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val currentFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Export") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text("TOTAL SPENT", fontSize = 14.sp, color = Color.Gray)
                Text("₹18,200", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("This Week", currentFilter == "This Week") { viewModel.setFilter("This Week") }
                    FilterChip("This Month", currentFilter == "This Month") { viewModel.setFilter("This Month") }
                    FilterChip("Custom", currentFilter == "Custom") { viewModel.setFilter("Custom") }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Text("TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            items(transactions.size) { index ->
                val t = transactions[index]
                TransactionRow(
                    title = t.category,
                    amount = if (t.isExpense) "-₹${t.amount.toInt()}" else "+₹${t.amount.toInt()}",
                    date = "14 Jun 2026"
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { /* TODO: Export PDF/CSV */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Export as PDF / CSV")
                }
            }
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) }
    )
}

@Composable
fun TransactionRow(title: String, amount: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }
        Text(
            amount,
            fontWeight = FontWeight.Bold,
            color = if (amount.startsWith("+")) Color(0xFF4CAF50) else Color.Red
        )
    }
}