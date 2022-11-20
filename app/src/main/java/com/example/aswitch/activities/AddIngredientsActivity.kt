package com.example.aswitch.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.adapters.AddIngredientAdapter
import com.example.aswitch.dialogs.QuantityDialog
import kotlinx.android.synthetic.main.activity_add_ingredients.*


class AddIngredientsActivity : AppCompatActivity(), QuantityDialog.ExampleDialogListener {

    private lateinit var adapter: AddIngredientAdapter
    private lateinit var extraIngredients: MutableList<Ingredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extraIngredients = intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>

        // Todo fetch from db
        val ingredients = mutableListOf<Ingredient>()
        ingredients.add(Ingredient("Szynka",""))
        ingredients.add(Ingredient("mleko",""))
        ingredients.add(Ingredient("jajko",""))

        adapter = AddIngredientAdapter(ingredients, this::openQuantityDialog)
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

    private fun openQuantityDialog(title: String) {
        val dialog = QuantityDialog(title)
        dialog.show(supportFragmentManager, "example dialog")
    }

    override fun applyTexts(username: String?, password: String?) {

    }

    override fun changeActivity(quantity: String, title: String) {
        Log.d("myTag", "This is my message $quantity $title")
        extraIngredients.add(Ingredient(title,quantity))
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("extra_ingredients", ArrayList(extraIngredients))
            startActivity(it)
        }
    }
}
