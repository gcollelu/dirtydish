package com.dirtydish.app.dirtydish

import java.util.*

data class Chore(val name: String = "chore",
                 var id: String = "",
                 var frequency: Int = 1,
                 var participants: MutableList<HouseMate> = mutableListOf())  {
}