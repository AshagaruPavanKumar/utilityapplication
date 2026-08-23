package com.utilityapplication.com.feature.finance.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FinanceRepository(private val database: AppDatabase) {
    private val dao = database.expenseDao()

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()

    suspend fun addExpense(expense: Expense) = dao.insertExpense(expense)

    fun getTotalSpent(start: Long, end: Long): Flow<Double> =
        dao.getTotalSpentBetween(start, end).map { it ?: 0.0 }

    fun getCategoryBreakdown(start: Long, end: Long): Flow<List<CategoryTotal>> =
        dao.getCategoryTotals(start, end)
}