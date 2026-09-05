package com.utilityapplication.com.feature.finance.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getExpensesBetween(start: Long, end: Long): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(expenses: List<Expense>)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM expenses WHERE isExpense = 1 AND date BETWEEN :start AND :end")
    fun getCountBetween(start: Long, end: Long): Flow<Int>

    @Query("SELECT * FROM expenses WHERE isRecurring = 1")
    suspend fun getRecurring(): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses WHERE isExpense = 1 AND date BETWEEN :start AND :end")
    fun getTotalSpentBetween(start: Long, end: Long): Flow<Double?>

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE isExpense = 1 AND date BETWEEN :start AND :end GROUP BY category")
    fun getCategoryTotals(start: Long, end: Long): Flow<List<CategoryTotal>>
}

data class CategoryTotal(val category: String, val total: Double)