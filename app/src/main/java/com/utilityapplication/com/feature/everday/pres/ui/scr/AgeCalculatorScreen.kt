package com.utilityapplication.com.feature.everday.pres.ui.scr

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.utilityapplication.com.navigation.AppBottomBar
import com.utilityapplication.com.navigation.MainNavigator
import com.utilityapplication.com.navigation.Routes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorScreen(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current
    var dateOfBirth by remember { mutableStateOf("05/15/1996") }
    var todayDate by remember { mutableStateOf("11/20/2024") }
    var showResult by remember { mutableStateOf(false) }

    // Calculated values
    var years by remember { mutableStateOf(28) }
    var months by remember { mutableStateOf(6) }
    var days by remember { mutableStateOf(5) }
    var totalDays by remember { mutableStateOf(10416) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Age Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        bottomBar = {
            AppBottomBar(
                currentRoute = Routes.EVERYDAY,
                onTabSelected = { route ->
                    if (route == Routes.EVERYDAY) onBackClick()
                    else MainNavigator.openTab(context, route)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Date Inputs
            Text("Date of Birth", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("MM/DD/YYYY") }
            )

            Spacer(Modifier.height(12.dp))

            Text("Today's Date", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = todayDate,
                onValueChange = { todayDate = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("MM/DD/YYYY") }
            )

            Spacer(Modifier.height(20.dp))

            // Calculate Button
            Button(
                onClick = {
                    // Simple calculation (you can improve this later)
                    showResult = true
                    // For demo, we keep hardcoded values
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Calculate Age", fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            // Result Section
            if (showResult) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOTAL AGE",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$years Years",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Born on Wednesday",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AgeBreakdownCard("$months", "Months")
                            AgeBreakdownCard("$days", "Days")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Total Days Lived
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Total Days Lived", fontSize = 14.sp, color = Color.Gray)
                            Text(
                                text = "%,d".format(totalDays),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Next Birthday
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Next Birthday", fontSize = 14.sp, color = Color.Gray)
                            Text("May 15", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text("5 Months 25 Days to go", fontSize = 13.sp, color = Color.Gray)
                        }
                        Icon(
                            Icons.Default.Cake,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Hours", "249,984", Icons.Default.AccessTime)
                    StatCard("Minutes", "14.9M+", Icons.Default.Timer)
                    StatCard("Heartbeats", "1.1B+", Icons.Default.Favorite)
                }
            }
        }
    }
}

@Composable
fun AgeBreakdownCard(value: String, label: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AgeCalculatorScreenPreview() {
    MaterialTheme {
        AgeCalculatorScreen()
    }
}