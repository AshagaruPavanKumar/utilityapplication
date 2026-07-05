@file:OptIn(ExperimentalMaterial3Api::class)

package com.utilityapplication.com.feature.everday.pres.ui.scr

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
fun UnitConverterScreen(onBackClick: () -> Unit = {}) {
    var selectedCategory by remember { mutableStateOf("Length") }
    var fromUnit by remember { mutableStateOf("Meters") }
    var toUnit by remember { mutableStateOf("Feet") }
    var inputValue by remember { mutableStateOf("1") }
    var result by remember { mutableStateOf("3.28084") }

    val categories = listOf("Length", "Weight", "Volume", "Temperature")

    // Unit options based on category
    val unitOptions = when (selectedCategory) {
        "Length" -> listOf("Meters", "Kilometers", "Centimeters", "Millimeters", "Feet", "Inches", "Miles")
        "Weight" -> listOf("Kilograms", "Grams", "Milligrams", "Pounds", "Ounces", "Tons")
        "Volume" -> listOf("Liters", "Milliliters", "Cubic Meters", "Gallons (US)", "Cups", "Fluid Ounces")
        "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
        else -> emptyList()
    }

    // Calculate result whenever input or units change
    LaunchedEffect(inputValue, fromUnit, toUnit, selectedCategory) {
        result = calculateConversion(
            inputValue.toDoubleOrNull() ?: 0.0,
            fromUnit,
            toUnit,
            selectedCategory
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Converter", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                        onClick = {
                            selectedCategory = category
                            // Reset units when category changes
                            fromUnit = unitOptions.firstOrNull() ?: ""
                            toUnit = unitOptions.getOrNull(1) ?: ""
                        },
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
                UnitDropdown(
                    selectedUnit = fromUnit,
                    units = unitOptions,
                    onUnitSelected = { fromUnit = it },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) inputValue = it },
                    modifier = Modifier.width(120.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Swap Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF1976D2), shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = "Swap",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // To Section
            Text("To", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UnitDropdown(
                    selectedUnit = toUnit,
                    units = unitOptions,
                    onUnitSelected = { toUnit = it },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(120.dp)
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

            Spacer(Modifier.height(32.dp))

            // Result Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "$inputValue $fromUnit = $result $toUnit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }
            }
        }
    }
}

@Composable
fun UnitDropdown(
    selectedUnit: String,
    units: List<String>,
    onUnitSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedUnit,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit) },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ==================== CONVERSION LOGIC ====================

fun calculateConversion(value: Double, from: String, to: String, category: String): String {
    if (value == 0.0) return "0"

    return try {
        val converted = when (category) {
            "Length" -> convertLength(value, from, to)
            "Weight" -> convertWeight(value, from, to)
            "Volume" -> convertVolume(value, from, to)
            "Temperature" -> convertTemperature(value, from, to)
            else -> value
        }
        String.format("%.6f", converted).trimEnd('0').trimEnd('.')
    } catch (e: Exception) {
        "Error"
    }
}

// Conversion functions (you can expand these)
private fun convertLength(value: Double, from: String, to: String): Double {
    val toMeter = when (from) {
        "Kilometers" -> value * 1000
        "Meters" -> value
        "Centimeters" -> value / 100
        "Millimeters" -> value / 1000
        "Feet" -> value * 0.3048
        "Inches" -> value * 0.0254
        "Miles" -> value * 1609.34
        else -> value
    }
    return when (to) {
        "Kilometers" -> toMeter / 1000
        "Meters" -> toMeter
        "Centimeters" -> toMeter * 100
        "Millimeters" -> toMeter * 1000
        "Feet" -> toMeter / 0.3048
        "Inches" -> toMeter / 0.0254
        "Miles" -> toMeter / 1609.34
        else -> toMeter
    }
}

private fun convertWeight(value: Double, from: String, to: String): Double {
    val toKg = when (from) {
        "Kilograms" -> value
        "Grams" -> value / 1000
        "Milligrams" -> value / 1_000_000
        "Pounds" -> value * 0.453592
        "Ounces" -> value * 0.0283495
        "Tons" -> value * 1000
        else -> value
    }
    return when (to) {
        "Kilograms" -> toKg
        "Grams" -> toKg * 1000
        "Milligrams" -> toKg * 1_000_000
        "Pounds" -> toKg / 0.453592
        "Ounces" -> toKg / 0.0283495
        "Tons" -> toKg / 1000
        else -> toKg
    }
}

private fun convertVolume(value: Double, from: String, to: String): Double {
    val toLiter = when (from) {
        "Liters" -> value
        "Milliliters" -> value / 1000
        "Cubic Meters" -> value * 1000
        "Gallons (US)" -> value * 3.78541
        "Cups" -> value * 0.236588
        "Fluid Ounces" -> value * 0.0295735
        else -> value
    }
    return when (to) {
        "Liters" -> toLiter
        "Milliliters" -> toLiter * 1000
        "Cubic Meters" -> toLiter / 1000
        "Gallons (US)" -> toLiter / 3.78541
        "Cups" -> toLiter / 0.236588
        "Fluid Ounces" -> toLiter / 0.0295735
        else -> toLiter
    }
}

private fun convertTemperature(value: Double, from: String, to: String): Double {
    val celsius = when (from) {
        "Celsius" -> value
        "Fahrenheit" -> (value - 32) * 5 / 9
        "Kelvin" -> value - 273.15
        else -> value
    }
    return when (to) {
        "Celsius" -> celsius
        "Fahrenheit" -> celsius * 9 / 5 + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }
}

// Bottom Navigation (unchanged)
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