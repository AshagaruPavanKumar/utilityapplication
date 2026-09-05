package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.core.util.DateRanges
import com.utilityapplication.com.feature.finance.data.CategoryTotal
import com.utilityapplication.com.feature.finance.data.Expense
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FinanceDashboardViewModel(private val repository: FinanceRepository) : ViewModel() {

    val recentTransactions: StateFlow<List<Expense>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalSpentThisMonth: StateFlow<Double> = repository.getTotalSpent(
        DateRanges.monthRange().first, DateRanges.monthRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayTotal: StateFlow<Double> = repository.getTotalSpent(
        DateRanges.startOfDay(), DateRanges.endOfDay()
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayCount: StateFlow<Int> = repository.getCount(
        DateRanges.startOfDay(), DateRanges.endOfDay()
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val weekTotal: StateFlow<Double> = repository.getTotalSpent(
        DateRanges.weekRange().first, DateRanges.weekRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val weekCount: StateFlow<Int> = repository.getCount(
        DateRanges.weekRange().first, DateRanges.weekRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val monthlyBreakdown: StateFlow<List<CategoryTotal>> = repository.getCategoryBreakdown(
        DateRanges.monthRange().first, DateRanges.monthRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
