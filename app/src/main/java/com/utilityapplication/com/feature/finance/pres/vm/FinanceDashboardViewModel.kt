package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.feature.finance.data.Expense
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.flow.*
import java.util.*

class FinanceDashboardViewModel(private val repository: FinanceRepository) : ViewModel() {

    val recentTransactions: StateFlow<List<Expense>> = repository.allExpenses
        .map { it.take(5) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSpentThisMonth: StateFlow<Double> = repository.getTotalSpent(
        getMonthStart(), getMonthEnd()
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private fun getMonthStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        return cal.timeInMillis
    }

    private fun getMonthEnd(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        return cal.timeInMillis
    }
}