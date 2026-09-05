package com.utilityapplication.com.feature.everday.pres.ui.frag

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.utilityapplication.com.feature.everday.pres.ui.act.AgeCalculatorActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.RandomToolsActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.UnitConverterActivity
import com.utilityapplication.com.feature.everday.pres.ui.scr.EverydayScreen
import com.utilityapplication.com.theme.UtilityAppTheme

class EverydayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UtilityAppTheme {
                    EverydayScreen(
                        onBackClick = { requireActivity().onBackPressedDispatcher.onBackPressed() },
                        onToolClick = { handleToolClick(it) }
                    )
                }
            }
        }
    }

    private fun handleToolClick(toolTitle: String) {
        val destination = when (toolTitle) {
            "Age Calculator" -> AgeCalculatorActivity::class.java
            "Unit Converter" -> UnitConverterActivity::class.java
            "Random Tools" -> RandomToolsActivity::class.java
            else -> null
        }
        if (destination != null) {
            startActivity(Intent(requireContext(), destination))
        }
    }
}
