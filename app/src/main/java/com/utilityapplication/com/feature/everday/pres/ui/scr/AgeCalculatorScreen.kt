package com.utilityapplication.com.feature.everday.pres.ui.scr

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utilityapplication.com.ads.InterstitialAds
import com.utilityapplication.com.core.analytics.AppAnalytics
import com.utilityapplication.com.core.ui.ToolScaffold
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

data class AgeResult(
    val years: Int,
    val months: Int,
    val days: Int,
    val totalDays: Long,
    val bornWeekday: String,
    val nextBirthdayLabel: String,
    val daysUntilBirthday: Int,
    val nextBirthdayWeekday: String
)

@Composable
fun AgeCalculatorScreen(
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val formatter = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    var dobMillis by remember {
        mutableLongStateOf(Calendar.getInstance().apply { set(1996, Calendar.MAY, 15) }.timeInMillis)
    }
    var todayMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var result by remember { mutableStateOf<AgeResult?>(null) }

    ToolScaffold(
        title = "Age Calculator",
        onBack = onBackClick,
        onSettings = onSettingsClick
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Date of Birth", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = formatter.format(Date(dobMillis)),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        showDatePicker(context, dobMillis) { dobMillis = it }
                    }) { Icon(Icons.Default.CalendarToday, contentDescription = "Pick date") }
                }
            )

            Spacer(Modifier.height(12.dp))

            Text("Today's Date", fontSize = 14.sp, color = Color.Gray)
            OutlinedTextField(
                value = formatter.format(Date(todayMillis)),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        showDatePicker(context, todayMillis) { todayMillis = it }
                    }) { Icon(Icons.Default.CalendarToday, contentDescription = "Pick date") }
                }
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    result = calculateAge(dobMillis, todayMillis)
                    AppAnalytics.logToolUsed("age_calculator")
                    InterstitialAds.showIfAvailable(context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Calculate Age", fontSize = 16.sp)
            }

            val current = result ?: return@Column
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TOTAL AGE", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text(
                        "${current.years} Years",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Born on ${current.bornWeekday}", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AgeBreakdownCard("${current.months}", "Months")
                        AgeBreakdownCard("${current.days}", "Days")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Days Lived", fontSize = 14.sp, color = Color.Gray)
                        Text("%,d".format(current.totalDays), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Next Birthday", fontSize = 14.sp, color = Color.Gray)
                        Text(current.nextBirthdayLabel, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "${current.daysUntilBirthday} days to go · ${current.nextBirthdayWeekday}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                    Icon(
                        Icons.Default.Cake,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Hours", "%,d".format(current.totalDays * 24), Icons.Default.AccessTime, Modifier.weight(1f))
                StatCard("Minutes", formatCompact(current.totalDays * 24 * 60), Icons.Default.Timer, Modifier.weight(1f))
                StatCard("Heartbeats", formatCompact(current.totalDays * 24 * 60 * 70), Icons.Default.Favorite, Modifier.weight(1f))
            }
        }
    }
}

private fun showDatePicker(context: android.content.Context, initial: Long, onPicked: (Long) -> Unit) {
    val cal = Calendar.getInstance().apply { timeInMillis = initial }
    DatePickerDialog(
        context,
        { _, year, month, day ->
            val next = Calendar.getInstance().apply {
                set(year, month, day, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            onPicked(next.timeInMillis)
        },
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun calculateAge(dobMillis: Long, todayMillis: Long): AgeResult {
    val dob = Calendar.getInstance().apply { timeInMillis = minOf(dobMillis, todayMillis) }
    val today = Calendar.getInstance().apply { timeInMillis = maxOf(dobMillis, todayMillis) }

    var years = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    var months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH)
    var days = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH)
    if (days < 0) {
        months--
        val prevMonth = Calendar.getInstance().apply {
            timeInMillis = today.timeInMillis
            add(Calendar.MONTH, -1)
        }
        days += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    if (months < 0) {
        years--
        months += 12
    }

    val totalDays = TimeUnit.MILLISECONDS.toDays(abs(todayMillis - dobMillis))
    val weekdayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())

    val nextBirthday = Calendar.getInstance().apply {
        timeInMillis = dob.timeInMillis
        set(Calendar.YEAR, today.get(Calendar.YEAR))
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    if (!nextBirthday.after(today)) {
        nextBirthday.add(Calendar.YEAR, 1)
    }
    val daysUntil = TimeUnit.MILLISECONDS.toDays(nextBirthday.timeInMillis - startOfDay(today.timeInMillis)).toInt()

    return AgeResult(
        years = years,
        months = months,
        days = days,
        totalDays = totalDays,
        bornWeekday = weekdayFormat.format(dob.time),
        nextBirthdayLabel = dateFormat.format(nextBirthday.time),
        daysUntilBirthday = daysUntil,
        nextBirthdayWeekday = weekdayFormat.format(nextBirthday.time)
    )
}

private fun startOfDay(millis: Long): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun formatCompact(value: Long): String {
    return when {
        value >= 1_000_000_000 -> String.format(Locale.getDefault(), "%.1fB+", value / 1_000_000_000.0)
        value >= 1_000_000 -> String.format(Locale.getDefault(), "%.1fM+", value / 1_000_000.0)
        else -> "%,d".format(value)
    }
}

@Composable
fun AgeBreakdownCard(value: String, label: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
