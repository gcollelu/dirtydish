package com.dirtydish.app.dirtydish

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Chore(val name: String = "chore",
                 var id: String = "",
                 var frequency: Int = 1,
                 var houseId: String = "",
                 var participants: MutableList<HouseMate> = mutableListOf()) : Parcelable