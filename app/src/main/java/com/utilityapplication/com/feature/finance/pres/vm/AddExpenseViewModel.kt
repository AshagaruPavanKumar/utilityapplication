package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.feature.finance.data.Expense
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val repository: FinanceRepository) : ViewModel() {

    val categories = EXPENSE_CATEGORIES

    fun saveExpense(
        amount: Double,
        category: String,
        dateMillis: Long,
        notes: String,
        isRecurring: Boolean = false,
        recurrence: String = ""
    ) {
        if (amount <= 0) return
        val expense = Expense(
            amount = amount,
            category = category,
            date = dateMillis,
            notes = notes,
            isRecurring = isRecurring,
            recurrence = if (isRecurring) recurrence else "",
            lastGenerated = if (isRecurring) dateMillis else 0L
        )
        viewModelScope.launch { repository.addExpense(expense) }
    }

    companion object {
        val EXPENSE_CATEGORIES = listOf(
            "Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Others"
        )
    }
}