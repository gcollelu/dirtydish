package com.dirtydish.app.dirtydish.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chore(val name: String = "chore",
                 var id: String = "",
                 var frequency: Int = 1,
                 var daysUntilRotation: Int = frequency,
                 var houseId: String = "",
                 var participants: MutableList<HouseMate> = mutableListOf(),
                 var description: String = "",
                 var startDate: String = "",
                 var endDate: String = "",
                 var assignee: String = "",
                 var image: String="") : Parcelable