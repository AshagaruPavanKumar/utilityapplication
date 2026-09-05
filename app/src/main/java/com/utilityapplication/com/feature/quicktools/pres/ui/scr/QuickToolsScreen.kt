package com.utilityapplication.com.feature.quicktools.pres.ui.scr

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utilityapplication.com.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickToolsScreen(
    onOpenTool: (String) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val tools = listOf(
        Routes.BMI to "BMI Calculator",
        Routes.PERCENTAGE to "Percentage Calculator",
        Routes.TIP to "Tip Calculator",
        Routes.DISCOUNT to "Discount Calculator",
        Routes.EMI to "EMI Calculator",
        Routes.GST to "GST Calculator",
        Routes.DATE_DIFF to "Date Difference"
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Tools", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            tools.forEach { (route, title) ->
                Card(
                    onClick = { onOpenTool(route) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null)
                        Spacer(Modifier.padding(8.dp))
                        Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
