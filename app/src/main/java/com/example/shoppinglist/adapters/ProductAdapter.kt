package com.example.shoppinglist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.HandlerCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.data.ProductDatabase
import com.example.shoppinglist.databinding.ListItemBinding
import com.example.shoppinglist.model.Product
import kotlin.concurrent.thread

class ProductViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(product: Product){
        binding.idText.text = product.id.toString()
        binding.productName.text = product.name
        binding.productNote.text = product.note
        binding.productImage.setImageResource(product.resId)
        binding.checkBox.isChecked = product.isTicked
        if(product.isTicked) {
            binding.constraintLayout.setBackgroundColor(Color.parseColor("#90EE90"))
        }else{
            binding.constraintLayout.setBackgroundColor(binding.root.cardBackgroundColor.defaultColor) //sorting
        }
    }
}

class ProductAdapter(private val con: Context, private val tv: TextView) : RecyclerView.Adapter<ProductViewHolder>(){
    private val data = mutableListOf<Product>()
    private val handler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())
    @SuppressLint("DiscouragedApi")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding).also {
            binding.checkBox.setOnClickListener {
                thread {
                    val product = ProductDatabase.open(con).products.getProduct(binding.idText.text.toString().toLong())
                    product.isTicked = binding.checkBox.isChecked
                    ProductDatabase.open(con).products.updateProduct(product)

                    (con as Activity).runOnUiThread{
                        if(product.isTicked){
                            binding.constraintLayout.setBackgroundColor(Color.parseColor("#90EE90"))
                        }else{
                            binding.constraintLayout.setBackgroundColor(binding.root.cardBackgroundColor.defaultColor)
                        }
                    }

                }
            }
            binding.root.setOnLongClickListener {
                deleteAlert(binding)
                return@setOnLongClickListener true
            }
            binding.root.setOnClickListener {
                thread {
                    val product = ProductDatabase.open(con).products.getProduct(binding.idText.text.toString().toLong())
                    val p = Product(
                        product.id,
                        product.name,
                        product.note,
                        product.isTicked,
                        con.resources.getIdentifier(product.icon,"drawable",con.packageName)
                    )
                    (con as? Navigable)?.navigate(Navigable.Destination.Edit, p)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])
    }

    private fun deleteAlert(binding: ListItemBinding) {
        val alertLayout = LayoutInflater.from(con).inflate(R.layout.alert_layout,null)

        val dialog = AlertDialog.Builder(con)
            .setView(alertLayout)
            .setPositiveButton("YES") { dialog, whichButton ->
                thread {
                    ProductDatabase.open(con).products.deleteProduct(binding.idText.text.toString().toLong())
                    loadDataSortedByName()
                }
                dialog.dismiss()}
            .setNegativeButton("NO") { dialog, whichButton ->
                dialog.dismiss()}

        dialog.show()
    }

    fun replace(newData: List<Product>){
        data.clear()
        data.addAll(newData)
        handler.post{
            notifyDataSetChanged()
        }
    }

    @SuppressLint("DiscouragedApi")
    fun loadData() = thread {
            val products = ProductDatabase.open(con).products.getAll().map {entity ->
                Product(
                    entity.id,
                    entity.name,
                    entity.note,
                    entity.isTicked,
                    con.resources.getIdentifier(entity.icon,"drawable",con.packageName)
                )
            }
            replace(products)
            (con as Activity).runOnUiThread{
                tv.text = products.size.toString()
            }
        }

    @SuppressLint("DiscouragedApi")
    fun loadDataSortedByName() = thread {
        val products = ProductDatabase.open(con).products.getAllSortedByName().map {entity ->
            Product(
                entity.id,
                entity.name,
                entity.note,
                entity.isTicked,
                con.resources.getIdentifier(entity.icon,"drawable",con.packageName)
            )
        }
        replace(products)
        (con as Activity).runOnUiThread{
            tv.text = products.size.toString()
        }
    }
}