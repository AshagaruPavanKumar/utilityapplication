package com.utilityapplication.com.feature.everday.pres.ui.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.utilityapplication.com.feature.everday.pres.ui.scr.RandomToolsScreen
import com.utilityapplication.com.theme.UtilityKitTheme
class RandomToolsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UtilityKitTheme {
                RandomToolsScreen(
                    onBackClick = { finish() }
                )
            }
        }
    }
}