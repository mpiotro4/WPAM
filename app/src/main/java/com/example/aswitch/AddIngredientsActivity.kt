package com.example.aswitch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_ingredients.*

class AddIngredientsActivity : AppCompatActivity() {

    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ingredients = mutableListOf<Ingredient>()
        ingredients.add(Ingredient("Szynka","plaster"))
        ingredients.add(Ingredient("mleko","200 ml"))
        ingredients.add(Ingredient("jajko","sztuka"))

        ingredientAdapter = IngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)
    }
}
