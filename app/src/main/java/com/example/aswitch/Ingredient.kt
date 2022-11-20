package com.example.aswitch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient (
    val title: String,
    var quantity: String
        ): Parcelable