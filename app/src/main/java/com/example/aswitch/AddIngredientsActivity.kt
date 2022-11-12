package com.example.aswitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.adapters.AddIngredientAdapter
import com.example.aswitch.adapters.IngredientAdapter
import kotlinx.android.synthetic.main.activity_add_ingredients.*

class AddIngredientsActivity : AppCompatActivity() {

    private lateinit var adapter: AddIngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ingredients = mutableListOf<Ingredient>()
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))

        adapter = AddIngredientAdapter(ingredients)
        rvIngredients.adapter = adapter
        rvIngredients.layoutManager = LinearLayoutManager(this)

        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                val filteredList = getFilteredList(newText, ingredients)
                adapter.setList(filteredList)
                return true
            }
        })
    }

    private fun getFilteredList(text: String, ingredients: MutableList<Ingredient>): MutableList<Ingredient> {
        val filteredList = mutableListOf<Ingredient>()
        ingredients.forEach {
            if(it.title.lowercase().contains(text.lowercase())) {
                filteredList.add(it)
            }
            if(filteredList.isEmpty()) {
                filteredList.add(Ingredient("Dodaj składnik \"$text\"",""))
            }
        }
        return filteredList
    }
}