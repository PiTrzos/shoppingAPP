package com.example.shoppinglist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.model.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM product;")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product ORDER BY name ASC;")
    fun getAllSortedByName(): List<ProductEntity>

    @Insert
    fun addProduct(newProduct: ProductEntity)

    @Update
    fun updateProduct(newProduct: ProductEntity)

    @Query("UPDATE product SET name = :name2, note=:note2, icon=:icon2 WHERE id = :passedID;")
    fun updateProductByID(passedID: Long, name2: String, note2: String, icon2: String)

    @Query("DELETE FROM product WHERE id = :passedID;")
    fun deleteProduct(passedID: Long)

    @Query("SELECT * FROM product WHERE id = :passedID;")
    fun getProduct(passedID: Long): ProductEntity
}