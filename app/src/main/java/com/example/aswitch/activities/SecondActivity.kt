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
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var ingredients = mutableListOf<Ingredient>()
        intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients")?.let {
            ingredients = intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        }

        ingredientAdapter = IngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)

        addChip("XD")
        addChip("dupa")
        addChip("wege")
        addChip("chuj")
        addChip("pizda")
        addChip("jeża")

        btnBack.setOnClickListener {
            finish()
        }

        etAddIngredients.setOnClickListener {
            Intent(this, AddIngredientsActivity::class.java).also {
                it.putExtra("extra_ingredients", ArrayList(ingredients))
                startActivity(it)
            }
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
            }
        }
    }
}