package com.utilityapplication.com.feature.finance.screen

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.utilityapplication.com.ads.RewardedAds
import com.utilityapplication.com.core.export.AppDataBackup
import com.utilityapplication.com.feature.finance.data.AppDatabase
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import com.utilityapplication.com.feature.finance.pres.vm.ReportsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val total = transactions.filter { it.isExpense }.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Export") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                Text("₹${"%.2f".format(total)}", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ReportFilterChip("This Week", currentFilter == "This Week") { viewModel.setFilter("This Week") }
                    ReportFilterChip("This Month", currentFilter == "This Month") { viewModel.setFilter("This Month") }
                    ReportFilterChip("All", currentFilter == "All") { viewModel.setFilter("All") }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item { Text("TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }
            items(transactions.size) { index ->
                val t = transactions[index]
                TransactionRow(
                    title = t.category,
                    amount = if (t.isExpense) "-₹${t.amount.toInt()}" else "+₹${t.amount.toInt()}",
                    date = dateFormat.format(Date(t.date))
                )
            }
            item {
                Spacer(Modifier.height(24.dp))
                OutlinedButton(
                    onClick = { AppDataBackup.shareCsv(context, transactions) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Export CSV (free)")
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        RewardedAds.show(
                            context = context,
                            onRewarded = {
                                val file = AppDataBackup.writePdf(context, transactions)
                                AppDataBackup.sharePdf(context, file)
                            },
                            onUnavailable = {
                                Toast.makeText(context, "Watch the ad to unlock PDF export", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Export PDF (watch ad)")
                }
            }
        }
    }
}

@Composable
fun ReportFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(text) })
}

@Composable
fun TransactionRow(title: String, amount: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
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
