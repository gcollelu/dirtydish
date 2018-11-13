package com.dirtydish.app.dirtydish

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class House(var name: String = "",
                 var address: String = "",
                 var id: String = "",
                 var houseMates: MutableList<HouseMate> = mutableListOf(),
                 var chores: MutableList<Chore> = mutableListOf()) : Parcelable