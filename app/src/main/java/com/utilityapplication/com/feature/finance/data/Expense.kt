package com.utilityapplication.com.feature.finance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: Long,
    val notes: String = "",
    val isExpense: Boolean = true,
    val isRecurring: Boolean = false,
    val recurrence: String = "",
    val lastGenerated: Long = 0L
)