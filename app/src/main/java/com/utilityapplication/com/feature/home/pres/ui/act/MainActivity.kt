package com.utilityapplication.com.feature.home.pres.ui.act

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.util.Consumer
import com.utilityapplication.com.navigation.AppNavigation
import com.utilityapplication.com.navigation.MainNavigator
import com.utilityapplication.com.navigation.Routes
import com.utilityapplication.com.theme.UtilityKitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val initialTab = intent.getStringExtra(MainNavigator.EXTRA_TAB) ?: Routes.HOME
            var requestedTab by remember { mutableStateOf(initialTab) }

            DisposableEffect(Unit) {
                val listener = Consumer<Intent> { newIntent ->
                    setIntent(newIntent)
                    requestedTab = newIntent.getStringExtra(MainNavigator.EXTRA_TAB) ?: Routes.HOME
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }

            UtilityKitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(startDestination = requestedTab)
                }
            }
        }
    }
}
