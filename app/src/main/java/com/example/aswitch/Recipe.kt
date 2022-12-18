package com.example.aswitch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe (
    val title: String,
    var cost: String
        ): Parcelable