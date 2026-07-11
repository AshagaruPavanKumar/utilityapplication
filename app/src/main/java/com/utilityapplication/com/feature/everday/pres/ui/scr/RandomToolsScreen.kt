package com.utilityapplication.com.feature.everday.pres.ui.scr

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomToolsScreen(onBackClick: () -> Unit = {}) {
    var selectedTab by remember { mutableStateOf("Coin") }
    var coinResult by remember { mutableStateOf("Heads") }
    var isFlipping by remember { mutableStateOf(false) }
    var diceResult by remember { mutableStateOf(6) }
    var randomNumber by remember { mutableStateOf(42) }
    var isRolling by remember { mutableStateOf(false) }

    val tabs = listOf("Coin", "Dice", "Number")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Tools", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

            Spacer(Modifier.height(48.dp))

            when (selectedTab) {
                "Coin" -> CoinTossSection(
                    coinResult = coinResult,
                    isFlipping = isFlipping,
                    onToss = {
                        isFlipping = true
                        // Simulate flip delay
                        MainScope().launch {
                            delay(600)
                            coinResult = if (Random.nextBoolean()) "Heads" else "Tails"
                            isFlipping = false
                        }
                    }
                )

                "Dice" -> DiceSection(
                    diceResult = diceResult,
                    isRolling = isRolling,
                    onRoll = {
                        isRolling = true
                        MainScope().launch {
                            delay(500)
                            diceResult = Random.nextInt(1, 7)
                            isRolling = false
                        }
                    }
                )

                "Number" -> RandomNumberSection(
                    randomNumber = randomNumber,
                    onGenerate = {
                        randomNumber = Random.nextInt(1, 101)
                    }
                )
            }
        }
    }
}

// ==================== COIN TOSS ====================
@Composable
fun CoinTossSection(
    coinResult: String,
    isFlipping: Boolean,
    onToss: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipping) 720f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = Modifier
            .size(160.dp)
            .rotate(rotation)
            .clip(CircleShape)
            .background(Color(0xFFFFD54F)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (coinResult == "Heads") "🪙" else "🔄",
            fontSize = 72.sp
        )
    }

    Spacer(Modifier.height(32.dp))

    Text(
        text = coinResult,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1976D2)
    )

    Spacer(Modifier.height(48.dp))

    Button(
        onClick = onToss,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
    ) {
        Text("Toss Coin", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ==================== DICE ====================
@Composable
fun DiceSection(diceResult: Int, isRolling: Boolean, onRoll: () -> Unit) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF424242)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🎲",
            fontSize = if (isRolling) 80.sp else 100.sp,
            color = Color.White
        )
    }

    Spacer(Modifier.height(24.dp))

    Text(
        text = diceResult.toString(),
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1976D2)
    )

    Spacer(Modifier.height(40.dp))

    Button(
        onClick = onRoll,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(if (isRolling) "Rolling..." else "Roll Dice", fontSize = 18.sp)
    }
}

// ==================== RANDOM NUMBER ====================
@Composable
fun RandomNumberSection(randomNumber: Int, onGenerate: () -> Unit) {
    Card(
        modifier = Modifier.size(200.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = randomNumber.toString(),
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
        }
    }

    Spacer(Modifier.height(40.dp))

    Button(
        onClick = onGenerate,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text("Generate New Number (1-100)", fontSize = 16.sp)
    }
}

// Bottom Navigation
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