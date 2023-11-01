package com.example.shoppinglist

import com.example.shoppinglist.model.Product

interface Navigable {
    enum class Destination{
        List, Add, Edit
    }
    fun navigate(to: Destination)
    fun navigate(to: Destination,product: Product)
}