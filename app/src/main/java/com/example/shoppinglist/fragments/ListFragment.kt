package com.example.shoppinglist.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.Navigable
import com.example.shoppinglist.ProductAdapter
import com.example.shoppinglist.data.ProductDatabase
import com.example.shoppinglist.databinding.FragmentListBinding
import com.example.shoppinglist.model.Product
import kotlin.concurrent.thread

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private var adapter: ProductAdapter? = null
    private var counter: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = context?.let { ProductAdapter(it,binding.productAmountText) }

        binding.shoppingList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btAdd.setOnClickListener{
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }
        binding.btSort.setOnClickListener{
            if(counter%2==0){
                loadData()
                counter++
            }else{
                loadDataSortedByName()
                counter = 0
            }
        }

    }

    @SuppressLint("DiscouragedApi")
    fun loadData() = thread {
            val products = ProductDatabase.open(requireContext()).products.getAll().map {entity ->
                Product(
                    entity.id,
                    entity.name,
                    entity.note,
                    entity.isTicked,
                    resources.getIdentifier(entity.icon,"drawable",requireContext().packageName)
                )
            }
            adapter?.replace(products)
            activity?.runOnUiThread {
                binding.productAmountText.text = products.size.toString()
            }
        }

    @SuppressLint("DiscouragedApi")
    fun loadDataSortedByName() = thread {
        val products = ProductDatabase.open(requireContext()).products.getAllSortedByName().map {entity ->
            Product(
                entity.id,
                entity.name,
                entity.note,
                entity.isTicked,
                resources.getIdentifier(entity.icon,"drawable",requireContext().packageName)
            )
        }
        adapter?.replace(products)
        activity?.runOnUiThread {
            binding.productAmountText.text = products.size.toString()
        }
    }

    override fun onStart() {
        super.onStart()
        loadDataSortedByName()
    }
}