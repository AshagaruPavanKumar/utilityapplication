package com.utilityapplication.com.feature.home.pres.ui.scr
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UtilityKitHomeScreen() {
        val context = LocalContext.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "UtilityKit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Settings */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = { UtilityBottomNavigation() }
        ) { innerPadding ->
            HomeContent(modifier = Modifier.padding(innerPadding),
                onEverydayClick = {
                    context.startActivity(
                        Intent(context, RandomToolsActivity::class.java)
                    )
                })
        }
    }

    @Composable
    fun HomeContent(modifier: Modifier = Modifier,
                    onEverydayClick: () -> Unit = {}   ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Greeting
            Text(
                text = "Good morning ☀️",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sunday, June 14",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            // Category Grid - Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryCard(
                    title = "Everyday",
                    icon = Icons.Default.CalendarToday,
                    iconTint = Color(0xFF1976D2),
                    containerColor = Color(0xFFBBDEFB),
                    modifier = Modifier.weight(1f).height(140.dp),
                    onClick = onEverydayClick   // ← Connected here
                )
                CategoryCard(
                    title = "Finance",
                    icon = Icons.Default.ShowChart,
                    iconTint = Color(0xFF388E3C),
                    containerColor = Color(0xFFC8E6C9),
                    modifier = Modifier.weight(1f).height(140.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Category Grid - Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryCard(
                    title = "Emergency",
                    icon = Icons.Default.Shield,
                    iconTint = Color(0xFFD32F2F),
                    containerColor = Color(0xFFFFCDD2),
                    badge = "Always Free",
                    modifier = Modifier.weight(1f).height(140.dp)
                )
                CategoryCard(
                    title = "Quick Tools",
                    icon = Icons.Default.Apps,
                    iconTint = Color(0xFFF57C00),
                    containerColor = Color(0xFFFFE0B2),
                    modifier = Modifier.weight(1f).height(140.dp)
                )
            }

            Spacer(Modifier.height(28.dp))

            // RECENT Section
            Text(
                text = "RECENT",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Unit Converter", "Coin Toss", "Ruler", "Level")) { item ->
                    RecentChip(text = item)
                }
            }

            Spacer(Modifier.height(28.dp))

            // DAILY TIP CARD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFF5C6BC0),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Daily Tip", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "Stay organized with our kit",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Thumbnail
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF37474F), Color(0xFF263238))
                                )
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.align(Alignment.Center).size(56.dp)
                        )

                        Text(
                            text = "How to use our 15+ new currency converters offline.",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .widthIn(max = 260.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(50.dp))
        }
    }

    @Composable
    fun CategoryCard(
        title: String,
        icon: ImageVector,
        iconTint: Color,
        containerColor: Color,
        badge: String? = null,
        modifier: Modifier = Modifier,
        onClick: () -> Unit = {}
    ) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E)
                    )
                }

                if (badge != null) {
                    Surface(
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 10.dp)
                    ) {
                        Text(
                            text = badge,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RecentChip(text: String, onClick: () -> Unit = {}) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            shadowElevation = 1.dp
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                fontSize = 14.sp
            )
        }
    }

    @Composable
    fun UtilityBottomNavigation() {
        NavigationBar {
            NavigationBarItem(
                selected = true,
                onClick = { },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 10.sp) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.GridView, contentDescription = "Everyday") },
                label = { Text("Everyday", fontSize = 10.sp) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.ShowChart, contentDescription = "Finance") },
                label = { Text("Finance", fontSize = 10.sp) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.Warning, contentDescription = "Emergency") },
                label = { Text("Emergency", fontSize = 10.sp) }
            )
            NavigationBarItem(
                selected = false,
                onClick = { },
                icon = { Icon(Icons.Default.Apps, contentDescription = "Quick Tools") },
                label = { Text("Quick Tools", fontSize = 10.sp) }
            )
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun UtilityKitHomeScreenPreview() {
        UtilityKitTheme {
            UtilityKitHomeScreen()
        }
    }