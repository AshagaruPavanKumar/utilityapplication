package com.utilityapplication.com.feature.everday.pres.ui.frag

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.utilityapplication.com.feature.everday.pres.ui.act.AgeCalculatorActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.RandomToolsActivity
import com.utilityapplication.com.feature.everday.pres.ui.act.UnitConverterActivity
import com.utilityapplication.com.feature.everday.pres.ui.scr.EverydayScreen
import com.utilityapplication.com.theme.UtilityKitTheme

class EverydayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                UtilityKitTheme {
                    EverydayScreen(
                        onBackClick = { requireActivity().onBackPressed() },
                        onToolClick = { toolTitle ->
                            handleToolClick(toolTitle)
                        }
                    )
                }
            }
        }
    }

    private fun handleToolClick(toolTitle: String) {
        when (toolTitle) {
            "Age Calculator" -> {
                // TODO: Navigate to Age Calculator
                // Example: findNavController().navigate(R.id.action_everyday_to_ageCalculator)
                startActivity(Intent(requireContext(), AgeCalculatorActivity::class.java))

            }
            "Unit Converter" -> {
                // Navigate to Unit Converter Activity / Fragment
                startActivity(Intent(requireContext(), UnitConverterActivity::class.java))
            }
            "Random Tools" -> {
                startActivity(Intent(requireContext(), RandomToolsActivity::class.java))
            }
            "Health Stats" -> {
                // TODO: Navigate to Health Stats
            }
        }
    }
}