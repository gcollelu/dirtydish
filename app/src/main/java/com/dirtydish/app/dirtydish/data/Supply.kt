package com.dirtydish.app.dirtydish.data


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Supply(var name: String = "", var missing: Boolean = false, var id: String = "", var houseId: String = "") : Parcelable