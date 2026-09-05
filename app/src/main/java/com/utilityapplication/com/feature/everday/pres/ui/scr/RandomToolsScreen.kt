package com.utilityapplication.com.feature.everday.pres.ui.scr

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utilityapplication.com.core.analytics.AppAnalytics
import com.utilityapplication.com.core.ui.ToolScaffold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RandomToolsScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Coin") }
    val tabs = listOf("Coin", "Dice", "Number")

    ToolScaffold(title = "Random Tools", onBack = onBackClick, onSettings = onSettingsClick) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        elevation = null
                    ) { Text(tab, fontSize = 14.sp) }
                }
            }
            Spacer(Modifier.height(32.dp))
            when (selectedTab) {
                "Coin" -> CoinTossSection()
                "Dice" -> DiceSection()
                "Number" -> RandomNumberSection()
            }
        }
    }
}

@Composable
private fun CoinTossSection() {
    var coinResult by remember { mutableStateOf("Heads") }
    var isFlipping by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val rotation by animateFloatAsState(
        targetValue = if (isFlipping) 720f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "coin"
    )

    Box(
        modifier = Modifier
            .size(160.dp)
            .rotate(rotation)
            .clip(CircleShape)
            .background(Color(0xFFFFD54F)),
        contentAlignment = Alignment.Center
    ) {
        Text(if (coinResult == "Heads") "H" else "T", fontSize = 64.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(Modifier.height(24.dp))
    Text(coinResult, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    Spacer(Modifier.height(32.dp))
    Button(
        onClick = {
            if (isFlipping) return@Button
            isFlipping = true
            scope.launch {
                delay(600)
                coinResult = if (Random.nextBoolean()) "Heads" else "Tails"
                isFlipping = false
                AppAnalytics.logToolUsed("coin_toss")
            }
        },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) { Text("Toss Coin", fontSize = 18.sp, fontWeight = FontWeight.SemiBold) }
}

@Composable
private fun DiceSection() {
    var diceCount by remember { mutableIntStateOf(1) }
    var results by remember { mutableStateOf(listOf(6)) }
    var isRolling by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Text("Number of dice", color = Color.Gray)
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..6).forEach { count ->
            FilterChip(
                selected = diceCount == count,
                onClick = { diceCount = count },
                label = { Text("$count") }
            )
        }
    }
    Spacer(Modifier.height(24.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        results.forEach { value ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF424242)),
                contentAlignment = Alignment.Center
            ) {
                Text("$value", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    Spacer(Modifier.height(16.dp))
    Text("Total: ${results.sum()}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = {
            if (isRolling) return@Button
            isRolling = true
            scope.launch {
                delay(400)
                results = List(diceCount) { Random.nextInt(1, 7) }
                isRolling = false
                AppAnalytics.logToolUsed("dice_roller")
            }
        },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) { Text(if (isRolling) "Rolling..." else "Roll Dice", fontSize = 18.sp) }
}

@Composable
private fun RandomNumberSection() {
    var minText by remember { mutableStateOf("1") }
    var maxText by remember { mutableStateOf("100") }
    var randomNumber by remember { mutableStateOf("42") }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = minText,
            onValueChange = { minText = it.filter { ch -> ch.isDigit() || ch == '-' } },
            label = { Text("Min") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        OutlinedTextField(
            value = maxText,
            onValueChange = { maxText = it.filter { ch -> ch.isDigit() || ch == '-' } },
            label = { Text("Max") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
    }
    Spacer(Modifier.height(24.dp))
    Card(
        modifier = Modifier.size(200.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(randomNumber, fontSize = 56.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
    Spacer(Modifier.height(24.dp))
    Button(
        onClick = {
            val min = minText.toIntOrNull() ?: 1
            val max = maxText.toIntOrNull() ?: 100
            val lo = minOf(min, max)
            val hi = maxOf(min, max)
            randomNumber = Random.nextInt(lo, hi + 1).toString()
            AppAnalytics.logToolUsed("random_number")
        },
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) { Text("Generate random number", fontSize = 16.sp) }
}
