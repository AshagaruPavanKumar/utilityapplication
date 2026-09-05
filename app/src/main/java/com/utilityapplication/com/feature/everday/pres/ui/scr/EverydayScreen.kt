package com.utilityapplication.com.feature.everday.pres.ui.scr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import android.content.Intent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utilityapplication.com.feature.everday.pres.ui.act.AgeCalculatorActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.RandomToolsActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.UnitConverterActivity
import com.utilityapplication.com.theme.UtilityKitTheme

data class DailyTool(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EverydayScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onToolClick: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val openTool: (String) -> Unit = onToolClick ?: { title ->
        when (title) {
            "Age Calculator" -> context.startActivity(
                Intent(context, AgeCalculatorActivity::class.java)
            )
            "Unit Converter" -> context.startActivity(
                Intent(context, UnitConverterActivity::class.java)
            )
            "Random Tools" -> context.startActivity(
                Intent(context, RandomToolsActivity::class.java)
            )
        }
    }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Everyday", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground,
                    navigationIconContentColor = colorScheme.onBackground,
                    actionIconContentColor = colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            // Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.primary)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Daily Utilities",
                            color = colorScheme.onPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Essential tools for your daily routine.",
                            color = colorScheme.onPrimary.copy(alpha = 0.9f),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // List Items
            items(getDailyTools()) { tool ->
                ToolListItem(
                    tool = tool,
                    onClick = { openTool(tool.title) }
                )
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
                        contentColor = colorScheme.primary
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
fun ToolListItem(tool: DailyTool, onClick: () -> Unit = {}) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainer),
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
                    .background(tool.iconTint.copy(alpha = 0.18f)),
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
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = tool.description,
                    fontSize = 13.sp,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = colorScheme.onSurfaceVariant
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
            iconTint = Color(0xFF1976D2)
        ),
        DailyTool(
            title = "Unit Converter",
            description = "Fast length, weight, and volume conversions",
            icon = Icons.Default.Straighten,
            iconTint = Color(0xFFFF9800)
        ),
        DailyTool(
            title = "Random Tools",
            description = "Dice roller, coin flip, and random numbers",
            icon = Icons.Default.Casino,
            iconTint = Color(0xFF4CAF50)
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EverydayScreenPreview() {
    UtilityKitTheme(darkTheme = false) {
        EverydayScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Everyday Dark")
@Composable
fun EverydayScreenDarkPreview() {
    UtilityKitTheme(darkTheme = true) {
        EverydayScreen()
    }
}