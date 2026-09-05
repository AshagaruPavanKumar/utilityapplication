package com.utilityapplication.com.feature.everday.pres.ui.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.utilityapplication.com.feature.everday.pres.ui.scr.AgeCalculatorScreen
import com.utilityapplication.com.navigation.MainNavigator
import com.utilityapplication.com.theme.UtilityAppTheme

class AgeCalculatorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtilityAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AgeCalculatorScreen(
                        onBackClick = { finish() },
                        onSettingsClick = { MainNavigator.openSettings(this) }
                    )
                }
            }
        }
    }
}