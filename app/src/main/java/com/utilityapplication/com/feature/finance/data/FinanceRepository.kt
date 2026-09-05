package com.utilityapplication.com.feature.finance.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FinanceRepository(private val database: AppDatabase) {
    private val dao = database.expenseDao()

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()

    suspend fun addExpense(expense: Expense) = dao.insertExpense(expense)

    suspend fun replaceAll(expenses: List<Expense>) {
        dao.deleteAll()
        if (expenses.isNotEmpty()) dao.insertAll(expenses)
    }

    fun getTotalSpent(start: Long, end: Long): Flow<Double> =
        dao.getTotalSpentBetween(start, end).map { it ?: 0.0 }

    fun getCount(start: Long, end: Long): Flow<Int> = dao.getCountBetween(start, end)

    fun getCategoryBreakdown(start: Long, end: Long): Flow<List<CategoryTotal>> =
        dao.getCategoryTotals(start, end)

    suspend fun processRecurring(now: Long = System.currentTimeMillis()) {
        dao.getRecurring().forEach { template ->
            var cursor = if (template.lastGenerated > 0) template.lastGenerated else template.date
            var last = template.lastGenerated
            while (true) {
                val next = com.utilityapplication.com.core.util.DateRanges.addRecurrence(
                    cursor,
                    template.recurrence.ifBlank { "monthly" }
                )
                if (next > now) break
                dao.insertExpense(
                    template.copy(
                        id = 0,
                        date = next,
                        isRecurring = false,
                        recurrence = "",
                        lastGenerated = 0L
                    )
                )
                last = next
                cursor = next
            }
            if (last != template.lastGenerated) {
                dao.updateExpense(template.copy(lastGenerated = last))
            }
        }
    }
}