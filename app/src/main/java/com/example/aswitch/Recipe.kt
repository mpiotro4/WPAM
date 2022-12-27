package com.example.aswitch

import java.io.Serializable

data class Recipe(
    val id: String,
    var title: String?,
    var cost: String?,
    var time: String?,
    var keyWords: List<String>
        ) : Serializable