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
fun FinanceDashboardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = { FinanceBottomNav() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                // Total Spent Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("SPENT THIS MONTH", color = Color.White.copy(0.8f), fontSize = 14.sp)
                        Text("₹1,240", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Text("↑ 12% from last month", color = Color.White.copy(0.8f), fontSize = 13.sp)
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
                    StatCard("This Month", "₹18,200", Modifier.weight(1f))
                }
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                Text("RECENT TRANSACTIONS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            item { Spacer(Modifier.height(8.dp)) }

            items(5) { index ->
                TransactionItem(
                    title = when (index) {
                        0 -> "Groceries"
                        1 -> "Lunch Out"
                        2 -> "Fuel Refill"
                        3 -> "Streaming Subscription"
                        else -> "Starbucks"
                    },
                    amount = when (index) {
                        0 -> "-₹340"
                        1 -> "-₹420"
                        2 -> "-₹1,200"
                        3 -> "-₹199"
                        else -> "-₹280"
                    },
                    date = "Today"
                )
            }

            item {
                TextButton(onClick = { /* TODO: See All */ }) {
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
        Text(amount, fontWeight = FontWeight.SemiBold, color = if (amount.startsWith("-")) Color.Red else Color.Green)
    }
}

@Composable
fun FinanceBottomNav() {
    NavigationBar {
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.GridView, null) }, label = { Text("Everyday") })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.ShowChart, null) }, label = { Text("Finance") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Warning, null) }, label = { Text("Emergency") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Apps, null) }, label = { Text("Quick Tools") })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FinanceDashboardPreview() {
    MaterialTheme {
        FinanceDashboardScreen()
    }
}