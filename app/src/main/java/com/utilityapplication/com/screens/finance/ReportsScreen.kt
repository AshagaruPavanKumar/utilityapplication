package com.utilityapplication.com.screens.finance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Export") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
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

            item { Spacer(Modifier.height(24.dp)) }

            item {
                Text("TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            items(6) { index ->
                TransactionRow(
                    title = when (index) {
                        0 -> "BigBasket Grocery"
                        1 -> "Electricity Bill"
                        2 -> "Zomato Order"
                        3 -> "Salary Credit"
                        else -> "Uber Ride"
                    },
                    amount = when (index) {
                        3 -> "+₹85,000"
                        else -> "-₹${(200..2500).random()}"
                    },
                    date = "14 Jun 2026"
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { /* TODO: Export Report */ },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportsScreenPreview() {
    MaterialTheme {
        ReportsScreen()
    }
}