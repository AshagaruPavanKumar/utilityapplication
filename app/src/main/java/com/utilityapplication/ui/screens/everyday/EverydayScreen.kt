package com.utilityapplication.ui.screens.everyday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DailyTool(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val backgroundColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EverydayScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Everyday", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Navigate back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = { EverydayBottomNavigation() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            // Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Daily Utilities",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Essential tools for your daily routine.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // List Items
            items(getDailyTools()) { tool ->
                ToolListItem(tool = tool)
            }

            // Request New Tool Button
            item {
                OutlinedButton(
                    onClick = { /* TODO: Request new tool */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1976D2)
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Request a new daily tool", fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun ToolListItem(tool: DailyTool) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tool.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = tool.title,
                    tint = tool.iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            // Title + Description
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = tool.description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = Color.Gray
            )
        }
    }
}

fun getDailyTools(): List<DailyTool> {
    return listOf(
        DailyTool(
            title = "Age Calculator",
            description = "Calculate exact age in years, months, days",
            icon = Icons.Default.CalendarToday,
            iconTint = Color(0xFF1976D2),
            backgroundColor = Color(0xFFE3F2FD)
        ),
        DailyTool(
            title = "Unit Converter",
            description = "Fast length, weight, and volume conversions",
            icon = Icons.Default.Straighten,
            iconTint = Color(0xFFFF9800),
            backgroundColor = Color(0xFFFFF3E0)
        ),
        DailyTool(
            title = "Random Tools",
            description = "Dice roller, coin flip, and random numbers",
            icon = Icons.Default.Casino,
            iconTint = Color(0xFF4CAF50),
            backgroundColor = Color(0xFFE8F5E9)
        ),
        DailyTool(
            title = "Health Stats",
            description = "Body Mass Index and hydration tracking",
            icon = Icons.Default.Favorite,
            iconTint = Color(0xFFE53935),
            backgroundColor = Color(0xFFFFEBEE)
        )
    )
}

@Composable
fun EverydayBottomNavigation() {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.GridView, contentDescription = "Everyday") },
            label = { Text("Everyday") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.ShowChart, contentDescription = "Finance") },
            label = { Text("Finance") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Warning, contentDescription = "Emergency") },
            label = { Text("Emergency") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Apps, contentDescription = "Quick Tools") },
            label = { Text("Quick Tools") }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EverydayScreenPreview() {
    MaterialTheme {
        EverydayScreen()
    }
}