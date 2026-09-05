package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.core.util.DateRanges
import com.utilityapplication.com.feature.finance.data.CategoryTotal
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ExpenseInsightsViewModel(private val repository: FinanceRepository) : ViewModel() {

    val totalSpent: StateFlow<Double> = repository.getTotalSpent(
        DateRanges.monthRange().first, DateRanges.monthRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val lastMonthSpent: StateFlow<Double> = repository.getTotalSpent(
        DateRanges.monthRange(-1).first, DateRanges.monthRange(-1).second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val categoryBreakdown: StateFlow<List<CategoryTotal>> = repository.getCategoryBreakdown(
        DateRanges.monthRange().first, DateRanges.monthRange().second
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthOverMonthPercent: StateFlow<Double> = combine(totalSpent, lastMonthSpent) { current, previous ->
        when {
            previous == 0.0 && current == 0.0 -> 0.0
            previous == 0.0 -> 100.0
            else -> ((current - previous) / previous) * 100.0
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}
