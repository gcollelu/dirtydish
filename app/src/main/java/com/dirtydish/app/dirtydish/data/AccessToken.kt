package com.dirtydish.app.dirtydish.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccessToken(var token : String = "") : Parcelable