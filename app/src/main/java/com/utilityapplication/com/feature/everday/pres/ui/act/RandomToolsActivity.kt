package com.utilityapplication.com.feature.everday.pres.ui.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.utilityapplication.com.feature.everday.pres.ui.scr.RandomToolsScreen
import com.utilityapplication.com.navigation.MainNavigator
import com.utilityapplication.com.theme.UtilityAppTheme
class RandomToolsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UtilityAppTheme {
                RandomToolsScreen(
                    onBackClick = { finish() },
                    onSettingsClick = { MainNavigator.openSettings(this) }
                )
            }
        }
    }
}