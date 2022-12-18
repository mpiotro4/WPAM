package com.example.aswitch.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.adapters.IngredientAdapter
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var keyWords: ArrayList<String>
    private lateinit var ingredients: MutableList<Ingredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        keyWords = arrayListOf()
        ingredients = mutableListOf()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val dbHelper = DBHelper(this, null)

        populateLists()

        ingredientAdapter = IngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener {
            finish()
        }

        etAddIngredients.setOnClickListener {
            Intent(this, AddIngredientsActivity::class.java).also {
                it.putExtra("extra_ingredients", ArrayList(ingredients))
                it.putExtra("extra_keyWords", keyWords)
                startActivity(it)
            }
        }

        etAddKeyWords.setOnClickListener {
            Intent(this, AddKeyWordsActivity::class.java).also {
                it.putExtra("extra_ingredients", ArrayList(ingredients))
                it.putExtra("extra_keyWords", keyWords)
                startActivity(it)
            }
        }

        btnAdd.setOnClickListener {
            dbHelper.addRecipe(
                etRecipeName.text.toString().ifEmpty{ null },
                etCost.text.toString(),
                etTime.text.toString(),
                keyWords,
                ingredients
            )
        }
    }

    private fun addChip(txt: String) {
        val chip = Chip(this)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                this@SecondActivity,
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            cgKeyWords.addView(chip as View)
            setOnCloseIconClickListener {
                cgKeyWords.removeView(chip as View)
                removeChip(txt)
            }
        }
    }

    private fun populateLists() {
        intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients")?.let {
            ingredients =
                intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        }
        intent.getStringArrayListExtra("extra_key_words")?.let {
            keyWords = it
            keyWords.forEach { addChip(it) }
        }
    }

    private fun removeChip(txt: String) {
        keyWords.remove(txt)
    }
}