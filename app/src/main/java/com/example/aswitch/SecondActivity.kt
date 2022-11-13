package com.example.aswitch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.adapters.IngredientAdapter
import com.example.aswitch.dialogs.QuantityDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.btnAdd
import kotlinx.android.synthetic.main.item_ingredient_add.*

class SecondActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val ingredients = mutableListOf<Ingredient>()
        ingredients.add(Ingredient("Szynka","plaster"))
        ingredients.add(Ingredient("mleko","200 ml"))
        ingredients.add(Ingredient("jajko","sztuka"))

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
            val intent = Intent(this, AddIngredientsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addChip(txt: String) {
        val chip = Chip(this)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                this@SecondActivity,
                R.drawable.ic_launcher_background)
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