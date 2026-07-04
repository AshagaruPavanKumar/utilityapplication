package com.utilityapplication.com.feature.everday.unitconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen() {
    var selectedCategory by remember { mutableStateOf("Length") }
    var fromUnit by remember { mutableStateOf("Meters (m)") }
    var toUnit by remember { mutableStateOf("Feet (ft)") }
    var inputValue by remember { mutableStateOf("1") }
    var result by remember { mutableStateOf("3.28084") }

    val categories = listOf("Length", "Weight", "Volume", "Temperature")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Converter", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Go back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Favorite */ }) {
                        Icon(Icons.Default.Star, contentDescription = "Favorite")
                    }
                }
            )
        },
        bottomBar = { UnitConverterBottomNav() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Category Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1976D2),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // From Section
            Text("From", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = fromUnit,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    modifier = Modifier.width(100.dp),
                    singleLine = true
                )
            }

            Spacer(Modifier.height(16.dp))

            // Swap Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        // Swap units
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF1976D2), shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = "Swap",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // To Section
            Text("To", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = toUnit,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = result,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Converted Value\n${toUnit}",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            // Favorites Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FAVORITES", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                TextButton(onClick = { /* TODO */ }) {
                    Text("Manage", color = Color(0xFF1976D2))
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("km → miles", "°C → °F", "kg → lbs")) { fav ->
                    AssistChip(
                        onClick = { /* TODO */ },
                        label = { Text(fav) }
                    )
                }
                item {
                    OutlinedButton(
                        onClick = { /* TODO: Add new favorite */ },
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("New")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Accurate Global Standards",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        "Real-time exchange rates & metrics",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun UnitConverterBottomNav() {
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
fun UnitConverterScreenPreview() {
    MaterialTheme {
        UnitConverterScreen()
    }
}