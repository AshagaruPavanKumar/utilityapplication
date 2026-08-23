package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.feature.finance.data.Expense
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.flow.*
import java.util.*

class ReportsViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("This Month")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val currentMonthRange = getMonthRange()
    private val currentWeekRange = getWeekRange()

    val transactions: StateFlow<List<Expense>> = combine(
        selectedFilter,
        repository.allExpenses
    ) { filter, allExpenses ->
        when (filter) {
            "This Week" -> {
                val (start, end) = currentWeekRange
                allExpenses.filter { it.date in start..end }
            }
            "This Month" -> {
                val (start, end) = currentMonthRange
                allExpenses.filter { it.date in start..end }
            }
            else -> allExpenses
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    private fun getMonthRange(): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val end = Calendar.getInstance().apply {
            add(Calendar.MONTH, 1)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MILLISECOND, -1)
        }.timeInMillis
        return start to end
    }

    private fun getWeekRange(): Pair<Long, Long> {
        val startCal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val end = Calendar.getInstance().apply {
            timeInMillis = startCal.timeInMillis
            add(Calendar.DAY_OF_WEEK, 7)
            add(Calendar.MILLISECOND, -1)
        }.timeInMillis
        return startCal.timeInMillis to end
    }
}