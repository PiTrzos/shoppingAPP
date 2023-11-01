package com.example.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ProductImageBinding

class ProductImageViewHolder(val binding: ProductImageBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(resId: Int, isSelected: Boolean){
        binding.image.setImageResource(resId)
        binding.selectedFrame.visibility =
            if(isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class ProductImageAdapter(pos: Int) : RecyclerView.Adapter<ProductImageViewHolder>(){
    var selectedPosition: Int = pos

    companion object{
        val data = listOf(
            R.drawable.dairy_food,
            R.drawable.fruitvege_food,
            R.drawable.meat_food,
            R.drawable.starchy_food,
            R.drawable.other_food)
    }

    val selectedIdRes : Int
        get() = data[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = ProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener{
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(data[position],position==selectedPosition)
    }
}