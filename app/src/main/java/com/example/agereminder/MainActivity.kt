package com.example.agereminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dayInput = findViewById<EditText>(R.id.dayInput)
        val monthInput = findViewById<EditText>(R.id.monthInput)
        val yearInput = findViewById<EditText>(R.id.yearInput)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        val prefs = getSharedPreferences("age_prefs", Context.MODE_PRIVATE)
        if (prefs.contains("day")) {
            dayInput.setText(prefs.getInt("day", 1).toString())
            monthInput.setText(prefs.getInt("month", 1).toString())
            yearInput.setText(prefs.getInt("year", 2000).toString())
            resultText.text = AgeUtils.buildMessage(
                prefs.getInt("day", 1), prefs.getInt("month", 1), prefs.getInt("year", 2000)
            )
        }

        saveButton.setOnClickListener {
            val day = dayInput.text.toString().toIntOrNull()
            val month = monthInput.text.toString().toIntOrNull()
            val year = yearInput.text.toString().toIntOrNull()

            if (day == null || month == null || year == null) {
                resultText.text = "الرجاء إدخال اليوم والشهر والسنة بشكل صحيح"
                return@setOnClickListener
            }

            prefs.edit()
                .putInt("day", day)
                .putInt("month", month)
                .putInt("year", year)
                .apply()

            resultText.text = AgeUtils.buildMessage(day, month, year)
            scheduleDailyAlarm()
        }
    }

    private fun scheduleDailyAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AgeNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
            )
        }
    }
}
