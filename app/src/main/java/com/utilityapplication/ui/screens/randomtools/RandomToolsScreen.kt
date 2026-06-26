package com.utilityapplication.ui.screens.randomtools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomToolsScreen() {
    var selectedTab by remember { mutableStateOf("Coin") }
    var coinResult by remember { mutableStateOf("Heads") }
    var isFlipping by remember { mutableStateOf(false) }

    val tabs = listOf("Coin", "Dice", "Number")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Tools", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Go back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = { RandomToolsBottomNav() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(50))
                    .padding(4.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Button(
                        onClick = { selectedTab = tab },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF1976D2) else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        elevation = null
                    ) {
                        Text(tab, fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Coin Toss Section
            if (selectedTab == "Coin") {
                // Coin
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD54F)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (coinResult == "Heads") "🪙" else "🔄",
                        fontSize = 60.sp
                    )
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = coinResult,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )

                Spacer(Modifier.height(32.dp))

                // Toss Button
                Button(
                    onClick = {
                        isFlipping = true
                        // Simulate flip
                        coinResult = if (Random.nextBoolean()) "Heads" else "Tails"
                        isFlipping = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Toss", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Dice Tab (Placeholder)
            if (selectedTab == "Dice") {
                Text("🎲 Dice Roller", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text("Coming soon...", color = Color.Gray)
            }

            // Number Tab (Placeholder)
            if (selectedTab == "Number") {
                Text("🔢 Random Number", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text("Coming soon...", color = Color.Gray)
            }
        }
    }
}

@Composable
fun RandomToolsBottomNav() {
    NavigationBar {
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.GridView, null) }, label = { Text("Everyday") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.ShowChart, null) }, label = { Text("Finance") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Warning, null) }, label = { Text("Emergency") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Apps, null) }, label = { Text("Quick Tools") })

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RandomToolsScreenPreview() {
    MaterialTheme {
        RandomToolsScreen()
    }
}