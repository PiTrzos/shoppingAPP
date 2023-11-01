package com.example.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shoppinglist.data.model.ProductEntity
import com.example.shoppinglist.databinding.ListItemBinding
import com.example.shoppinglist.fragments.EditFragment
import com.example.shoppinglist.fragments.ListFragment
import com.example.shoppinglist.model.Product

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.ConstraintLayout,listFragment,listFragment.javaClass.name)
            .commit()

    }

    override fun navigate(to: Navigable.Destination){
        supportFragmentManager.beginTransaction().apply {
            when(to){
                Navigable.Destination.List -> replace(R.id.ConstraintLayout,listFragment,listFragment.javaClass.name)
                Navigable.Destination.Add -> {
                    replace(R.id.ConstraintLayout, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Edit ->{
                    val fragment = EditFragment()
                    replace(R.id.ConstraintLayout, fragment, fragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
            }
        }.commit()
    }

    override fun navigate(to: Navigable.Destination, product: Product) {
        supportFragmentManager.beginTransaction().apply {
            when(to){
                Navigable.Destination.List -> replace(R.id.ConstraintLayout,listFragment,listFragment.javaClass.name)
                Navigable.Destination.Add -> {
                    replace(R.id.ConstraintLayout, EditFragment(), EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Edit ->{
                    val fragment = EditFragment()
                    fragment.dataFragment = product
                    replace(R.id.ConstraintLayout, fragment, fragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
            }
        }.commit()
    }

}