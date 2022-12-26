package com.example.aswitch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.IngredientAdapter
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_read_recipe.*

class ReadRecipeActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var keyWords: ArrayList<String>
    private lateinit var ingredients: MutableList<Ingredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        keyWords = arrayListOf()
        ingredients = mutableListOf()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_recipe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dbHelper = DBHelper(this, null)

        val recipe = intent.extras?.get("extra_recipe") as Recipe
        recipe.keyWords.forEach { addChip(it) }

        populateIngredientsRv(dbHelper, recipe)

        etRecipeName.setText(recipe.title)
        etCost.setText(recipe.cost)
        etTime.setText(recipe.time)
    }

    private fun populateIngredientsRv(dbHelper: DBHelper, recipe: Recipe) {
        ingredients = dbHelper.getRecipeIngrediens(recipe.id) as MutableList<Ingredient>
        ingredientAdapter = IngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu :Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.read_recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun addChip(txt: String) {
        val chip = Chip(this)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                this@ReadRecipeActivity,
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = false
            isClickable = false
            isCheckable = false
            cgKeyWords.addView(chip as View)
        }
    }
}