package com.dirtydish.app.dirtydish.singletons

import java.util.regex.Pattern

object Utilities {

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    fun intFrequencyToString(freq: Int): String {
        var frequency_text = ""
        if (freq % 7 == 0) {
            frequency_text = (freq / 7).toString()
            if (freq == 7) frequency_text = "week"
            else frequency_text += " weeks"

        } else if (freq % 30 == 0) {
            frequency_text = (freq / 30).toString()
            if (freq == 30) frequency_text = "month"
            else frequency_text += " months"

        } else {
            frequency_text = freq.toString()
            if (freq == 1) frequency_text = "day"
            else frequency_text += " days"
        }
        return frequency_text
    }
}