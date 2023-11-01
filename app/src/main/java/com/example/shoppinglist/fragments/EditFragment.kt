package com.example.shoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.Navigable
import com.example.shoppinglist.ProductAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.adapters.ProductImageAdapter
import com.example.shoppinglist.data.ProductDatabase
import com.example.shoppinglist.data.model.ProductEntity
import com.example.shoppinglist.databinding.FragmentEditBinding
import com.example.shoppinglist.model.Product
import kotlin.concurrent.thread

class EditFragment() : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ProductImageAdapter

    var dataFragment: Product = Product(-1,"","",false,-1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(dataFragment.id != -1L){

            adapter = ProductImageAdapter(dataFragment.resId)
            var counter = 0
            for(resId: Int in ProductImageAdapter.data){
                if(resId == dataFragment.resId){
                    adapter = ProductImageAdapter(counter)            //setImage
                    break
                }
                counter++
            }

            binding.productName.setText(dataFragment.name, TextView.BufferType.EDITABLE)
            binding.productNote.setText(dataFragment.note, TextView.BufferType.EDITABLE)

            binding.saveButton.setOnClickListener{
                thread {
                    ProductDatabase.open(requireContext()).products.updateProductByID(
                        dataFragment.id,
                        binding.productName.text.toString(),
                        binding.productNote.text.toString(),
                        resources.getResourceEntryName(adapter.selectedIdRes)
                    )
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            }
        }else{
            adapter = ProductImageAdapter(4) //default
            binding.saveButton.setOnClickListener{
                val newProduct = ProductEntity(
                    name = binding.productName.text.toString(),
                    note = binding.productNote.text.toString(),
                    isTicked = false,
                    icon = resources.getResourceEntryName(adapter.selectedIdRes)
                )

                thread {
                    ProductDatabase.open(requireContext()).products.addProduct(newProduct)
                    (activity as? Navigable)?.navigate(Navigable.Destination.List)
                }
            }
        }
        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }

    }
}
