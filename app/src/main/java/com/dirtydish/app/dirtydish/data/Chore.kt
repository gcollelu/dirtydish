package com.dirtydish.app.dirtydish.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chore(val name: String = "chore",
                 var id: String = "",
                 var frequency: Int = 1,
                 var houseId: String = "",
                 var participants: MutableList<HouseMate> = mutableListOf(),
                 var description: String = "",
                 var startDate: String = "",
                 var endDate: String = "") : Parcelable