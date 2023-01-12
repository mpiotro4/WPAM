package com.example.aswitch.activities

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.ReadIngredientAdapter
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_read_recipe.*
import kotlinx.android.synthetic.main.activity_read_recipe.cgKeyWords
import kotlinx.android.synthetic.main.activity_read_recipe.etCost
import kotlinx.android.synthetic.main.activity_read_recipe.etRecipeName
import kotlinx.android.synthetic.main.activity_read_recipe.etTime
import kotlinx.android.synthetic.main.activity_read_recipe.rvIngredients
import java.io.Serializable

class ReadRecipeActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: ReadIngredientAdapter
    private lateinit var keyWords: ArrayList<String>
    private lateinit var ingredients: MutableList<Ingredient>
    private lateinit var recipe: Recipe
    private val dbHelper = DBHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        keyWords = arrayListOf()
        ingredients = mutableListOf()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_recipe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recipe = intent.extras?.get("extra_recipe") as Recipe
        keyWords = recipe.keyWords as ArrayList<String>
        val byteArray = dbHelper.getRecipeImageById(recipe.id)
        val bmp = byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.lastIndex) }
        imageView.setImageBitmap(bmp)

        populateUIElements(dbHelper, recipe)
    }

    private fun populateUIElements(dbHelper: DBHelper, recipe: Recipe) {
        ingredients = dbHelper.getRecipeIngrediens(recipe.id) as MutableList<Ingredient>
        ingredientAdapter = ReadIngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)

        etRecipeName.setText(recipe.title)
        etCost.setText(recipe.cost)
        etTime.setText(recipe.time)
        etDescription.setText(recipe.description)

        recipe.keyWords?.forEach { addChip(it) }
    }

    override fun onCreateOptionsMenu(menu :Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.read_recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.updateRecipe) {
            Intent(this, SecondActivity::class.java).also {
                it.putExtra("extra_recipe", recipe as Serializable)
                it.putExtra("extra_ingredients", ArrayList(ingredients))
                it.putExtra("extra_key_words", keyWords)
                it.putExtra("extra_if_update", true)
                it.putExtra("extra_img", dbHelper.getRecipeImageById(recipe.id))
                startActivity(it)
            }
        }
        if(item.itemId == R.id.deleteRecipe){
            dbHelper.deleteRecipe(recipe.id)
            Toast.makeText(applicationContext,"Przepis usunięty pomyślnie", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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