package com.dirtydish.app.dirtydish

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseMate(val name: String = "", val email: String = "", var id: String = "", var house: House = House()) : Parcelable