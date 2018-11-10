package com.dirtydish.app.dirtydish

import java.util.*

data class Chore(val name: String = "chore",
                 var id: String = "",
                 var frequency: Int = 1)  {
    //TODO: add extra fields (frequency, responsible people)
}