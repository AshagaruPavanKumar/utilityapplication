package com.utilityapplication.ui.screens.finance

import androidx.compose.foundation.layout.*
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
fun ExpenseInsightsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Insights", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { FinanceBottomNav() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("JUNE 2026", color = Color.White.copy(0.8f), fontSize = 14.sp)
                    Text("₹6,600", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text("Total Spent", color = Color.White.copy(0.85f), fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("INSIGHTS", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

            Spacer(Modifier.height(12.dp))

            // Insight Cards
            InsightCard(
                icon = Icons.Default.Restaurant,
                title = "Food",
                amount = "₹2,200",
                percentage = "33%",
                color = Color(0xFF4CAF50)
            )

            InsightCard(
                icon = Icons.Default.DirectionsCar,
                title = "Travel",
                amount = "₹1,800",
                percentage = "27%",
                color = Color(0xFF2196F3)
            )

            InsightCard(
                icon = Icons.Default.Receipt,
                title = "Bills",
                amount = "₹1,500",
                percentage = "23%",
                color = Color(0xFFFF9800)
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("You spent ₹2,200 on Food this month", fontWeight = FontWeight.Medium)
                    Text("This is your highest spending category.", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun InsightCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, amount: String, percentage: String, color: Color) {
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
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)
                Text(amount, color = Color.Gray, fontSize = 13.sp)
            }
            Text(percentage, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseInsightsPreview() {
    MaterialTheme {
        ExpenseInsightsScreen()
    }
}