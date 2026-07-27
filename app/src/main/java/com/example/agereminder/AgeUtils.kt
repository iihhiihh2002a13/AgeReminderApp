package com.example.agereminder

import java.util.Calendar

object AgeUtils {

    fun buildMessage(day: Int, month: Int, year: Int): String {
        val birth = Calendar.getInstance().apply {
            set(year, month - 1, day, 0, 0, 0)
        }
        val now = Calendar.getInstance()

        var years = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)
        var months = now.get(Calendar.MONTH) - birth.get(Calendar.MONTH)
        var days = now.get(Calendar.DAY_OF_MONTH) - birth.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            months -= 1
            val lastMonth = Calendar.getInstance().apply {
                set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1)
                add(Calendar.DAY_OF_MONTH, -1)
            }
            days += lastMonth.get(Calendar.DAY_OF_MONTH)
        }
        if (months < 0) {
            years -= 1
            months += 12
        }

        return "🎂 عمرك اليوم: $years سنة و$months شهر و$days يوم"
    }
}
