package com.utilityapplication.com.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.utilityapplication.com.feature.home.pres.ui.act.MainActivity

object MainNavigator {
    const val EXTRA_TAB = "extra_tab"

    fun openTab(context: Context, route: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_TAB, route)
        }
        context.startActivity(intent)
        if (context is Activity && context !is MainActivity) {
            context.finish()
        }
    }
}
