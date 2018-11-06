package com.dirtydish.app.dirtydish

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class House(val name: String = "", val address: String = "", var id: String = "", var houseMates : Array<HouseMate> = arrayOf()) : Parcelable