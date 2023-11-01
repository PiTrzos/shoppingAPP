package com.example.shoppinglist.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Long,
    val name: String,
    val note: String,
    var isTicked: Boolean,
    @DrawableRes
    val resId: Int
)
