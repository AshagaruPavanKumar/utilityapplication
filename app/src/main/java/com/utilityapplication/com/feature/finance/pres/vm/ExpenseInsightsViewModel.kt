package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.feature.finance.data.CategoryTotal
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.flow.*
import java.util.*

class ExpenseInsightsViewModel(private val repository: FinanceRepository) : ViewModel() {

    val totalSpent: StateFlow<Double> = repository.getTotalSpent(getMonthStart(), getMonthEnd())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val categoryBreakdown: StateFlow<List<CategoryTotal>> = repository.getCategoryBreakdown(getMonthStart(), getMonthEnd())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun getMonthStart(): Long { /* same as above */
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        return cal.timeInMillis
    }

    private fun getMonthEnd(): Long { /* same as above */
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        return cal.timeInMillis
    }
}