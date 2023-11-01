package com.example.shoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    var isTicked: Boolean,
    val note: String,
    val icon: String
)
