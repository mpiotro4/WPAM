package com.example.aswitch.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_key_words.*

class AddKeyWordsActivity : AppCompatActivity() {
    private lateinit var extraIngredients: MutableList<Ingredient>
    private lateinit var extraKeyWords: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_key_words)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extraIngredients = intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        extraKeyWords = intent.getStringArrayListExtra("extra_keyWords") as ArrayList<String>

        val txts = mutableListOf<String>()
        txts.add("chuj")
        txts.add("dupa")
        txts.add("pizda")
        txts.add("jeża")

        txts.forEach{ addChip(it) }
    }

    private fun addChip(txt: String) {
        val chip = Chip(this)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                this@AddKeyWordsActivity,
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = false
            isClickable = true
            isCheckable = false
            cgKeyWords.addView(chip as View)
            setOnCloseIconClickListener {
                cgKeyWords.removeView(chip as View)
            }
            setOnClickListener {
                extraKeyWords.add(text.toString())
                changeActivity()
            }
        }
    }

    private fun changeActivity() {
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("extra_ingredients", ArrayList(extraIngredients))
            it.putExtra("extra_key_words", ArrayList(extraKeyWords))
            startActivity(it)
        }
    }
}
