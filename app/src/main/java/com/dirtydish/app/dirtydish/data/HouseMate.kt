package com.dirtydish.app.dirtydish.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseMate(var name: String = "", var email: String = "", var id: String = "", var houseId: String = "") : Parcelable