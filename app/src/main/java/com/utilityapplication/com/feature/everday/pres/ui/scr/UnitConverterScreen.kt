@file:OptIn(ExperimentalMaterial3Api::class)

package com.utilityapplication.com.feature.everday.pres.ui.scr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utilityapplication.com.core.prefs.AppPreferences
import com.utilityapplication.com.core.ui.ToolScaffold

private val converterCategories = listOf(
    "Length", "Weight", "Temperature", "Speed", "Area", "Volume", "Time", "Data Storage"
)

@Composable
fun UnitConverterScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { AppPreferences.get(context) }
    val favorites by prefs.favorites.collectAsState()

    var selectedCategory by remember { mutableStateOf("Length") }
    var fromUnit by remember { mutableStateOf("Meters") }
    var toUnit by remember { mutableStateOf("Feet") }
    var inputValue by remember { mutableStateOf("1") }
    var result by remember { mutableStateOf("3.28084") }

    val unitOptions = unitsFor(selectedCategory)
    val pairKey = "$selectedCategory|$fromUnit|$toUnit"

    LaunchedEffect(inputValue, fromUnit, toUnit, selectedCategory) {
        result = calculateConversion(
            inputValue.toDoubleOrNull() ?: 0.0,
            fromUnit,
            toUnit,
            selectedCategory
        )
    }

    ToolScaffold(title = "Unit Converter", onBack = onBackClick, onSettings = onSettingsClick) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (favorites.isNotEmpty()) {
                Text("Favorites", fontSize = 13.sp, color = Color.Gray)
                Spacer(Modifier.height(6.dp))
                favorites.forEach { key ->
                    val parts = key.split("|")
                    if (parts.size == 3) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable {
                                    selectedCategory = parts[0]
                                    fromUnit = parts[1]
                                    toUnit = parts[2]
                                },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "${parts[0]}: ${parts[1]} → ${parts[2]}",
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(converterCategories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            val units = unitsFor(category)
                            fromUnit = units.first()
                            toUnit = units.getOrElse(1) { units.first() }
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text("From", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                UnitDropdown(fromUnit, unitOptions, { fromUnit = it }, Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) inputValue = it },
                    modifier = Modifier.width(120.dp),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = {
                        val temp = fromUnit
                        fromUnit = toUnit
                        toUnit = temp
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(Modifier.height(16.dp))

            Text("To", fontSize = 14.sp, color = Color.Gray)
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                UnitDropdown(toUnit, unitOptions, { toUnit = it }, Modifier.weight(1f))
                Spacer(Modifier.width(12.dp))
                Text(
                    text = result,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(120.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { prefs.toggleFavorite(pairKey) }) {
                    Icon(
                        if (favorites.contains(pairKey)) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = Color(0xFFFFA000)
                    )
                }
                Text(if (favorites.contains(pairKey)) "Saved to favorites" else "Save this conversion")
            }

            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Result", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$inputValue $fromUnit = $result $toUnit",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = modifier) {
        OutlinedTextField(
            value = selectedUnit,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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

fun unitsFor(category: String): List<String> = when (category) {
    "Length" -> listOf("Meters", "Kilometers", "Centimeters", "Millimeters", "Feet", "Inches", "Miles")
    "Weight" -> listOf("Kilograms", "Grams", "Milligrams", "Pounds", "Ounces", "Tons")
    "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
    "Speed" -> listOf("m/s", "km/h", "mph", "knots")
    "Area" -> listOf("Square Meters", "Square Kilometers", "Square Feet", "Acres", "Hectares")
    "Volume" -> listOf("Liters", "Milliliters", "Cubic Meters", "Gallons (US)", "Cups", "Fluid Ounces")
    "Time" -> listOf("Seconds", "Minutes", "Hours", "Days", "Weeks")
    "Data Storage" -> listOf("Bytes", "Kilobytes", "Megabytes", "Gigabytes", "Terabytes")
    else -> emptyList()
}

fun calculateConversion(value: Double, from: String, to: String, category: String): String {
    if (from == to) return formatNumber(value)
    return try {
        val converted = when (category) {
            "Length" -> convertLinear(value, from, to, lengthToMeter)
            "Weight" -> convertLinear(value, from, to, weightToKg)
            "Volume" -> convertLinear(value, from, to, volumeToLiter)
            "Speed" -> convertLinear(value, from, to, speedToMs)
            "Area" -> convertLinear(value, from, to, areaToSqm)
            "Time" -> convertLinear(value, from, to, timeToSeconds)
            "Data Storage" -> convertLinear(value, from, to, dataToBytes)
            "Temperature" -> convertTemperature(value, from, to)
            else -> value
        }
        formatNumber(converted)
    } catch (_: Exception) {
        "Error"
    }
}

private fun formatNumber(value: Double): String =
    String.format("%.6f", value).trimEnd('0').trimEnd('.')

private fun convertLinear(value: Double, from: String, to: String, table: Map<String, Double>): Double {
    val base = value * (table[from] ?: 1.0)
    return base / (table[to] ?: 1.0)
}

private val lengthToMeter = mapOf(
    "Kilometers" to 1000.0, "Meters" to 1.0, "Centimeters" to 0.01, "Millimeters" to 0.001,
    "Feet" to 0.3048, "Inches" to 0.0254, "Miles" to 1609.34
)
private val weightToKg = mapOf(
    "Kilograms" to 1.0, "Grams" to 0.001, "Milligrams" to 0.000001,
    "Pounds" to 0.453592, "Ounces" to 0.0283495, "Tons" to 1000.0
)
private val volumeToLiter = mapOf(
    "Liters" to 1.0, "Milliliters" to 0.001, "Cubic Meters" to 1000.0,
    "Gallons (US)" to 3.78541, "Cups" to 0.236588, "Fluid Ounces" to 0.0295735
)
private val speedToMs = mapOf("m/s" to 1.0, "km/h" to 1 / 3.6, "mph" to 0.44704, "knots" to 0.514444)
private val areaToSqm = mapOf(
    "Square Meters" to 1.0, "Square Kilometers" to 1_000_000.0, "Square Feet" to 0.092903,
    "Acres" to 4046.86, "Hectares" to 10_000.0
)
private val timeToSeconds = mapOf(
    "Seconds" to 1.0, "Minutes" to 60.0, "Hours" to 3600.0, "Days" to 86400.0, "Weeks" to 604800.0
)
private val dataToBytes = mapOf(
    "Bytes" to 1.0, "Kilobytes" to 1024.0, "Megabytes" to 1024.0 * 1024,
    "Gigabytes" to 1024.0 * 1024 * 1024, "Terabytes" to 1024.0 * 1024 * 1024 * 1024
)

private fun convertTemperature(value: Double, from: String, to: String): Double {
    val celsius = when (from) {
        "Fahrenheit" -> (value - 32) * 5 / 9
        "Kelvin" -> value - 273.15
        else -> value
    }
    return when (to) {
        "Fahrenheit" -> celsius * 9 / 5 + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }
}
