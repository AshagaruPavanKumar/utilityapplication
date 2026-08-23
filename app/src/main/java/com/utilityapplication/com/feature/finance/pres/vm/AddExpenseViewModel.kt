package com.utilityapplication.com.feature.finance.pres.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utilityapplication.com.feature.finance.data.Expense
import com.utilityapplication.com.feature.finance.data.FinanceRepository
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val repository: FinanceRepository) : ViewModel() {

    val categories = listOf("Food", "Travel", "Shopping", "Bills", "Entertainment", "Others")

    fun saveExpense(amount: Double, category: String, dateMillis: Long, notes: String) {
        if (amount <= 0) return
        val expense = Expense(amount = amount, category = category, date = dateMillis, notes = notes)
        viewModelScope.launch { repository.addExpense(expense) }
    }
}