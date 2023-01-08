package com.example.aswitch.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_add_key_words.*
import java.io.Serializable

class AddKeyWordsActivity : AppCompatActivity() {
    private lateinit var extraIngredients: MutableList<Ingredient>
    private lateinit var extraKeyWords: MutableList<String>
    private lateinit var extraRecipe: Recipe
    private var ifUpadte = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_key_words)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extraIngredients = intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        extraKeyWords = intent.getStringArrayListExtra("extra_keyWords") as ArrayList<String>
        if(intent.extras?.get("extra_recipe") != null) {
            extraRecipe = intent.extras?.get("extra_recipe") as Recipe
        }
        ifUpadte = intent.getBooleanExtra("extra_if_update", false)

        val keywords = fetchKeywords()
        keywords.forEach{ addChip(it) }
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

    private fun fetchKeywords(): MutableList<String> {
        val keywords = mutableListOf<String>()
        val db = DBHelper(this, null)
        val cursor = db.getKeyword()
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {
            keywords.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEYWORDS_KEYWORD_COL)))
        }
        return keywords
    }

    private fun changeActivity() {
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("extra_ingredients", ArrayList(extraIngredients))
            it.putExtra("extra_key_words", ArrayList(extraKeyWords))
            if (this::extraRecipe.isInitialized) {
                it.putExtra("extra_recipe", extraRecipe as Serializable)
            }
            it.putExtra("extra_if_update", ifUpadte)
            startActivity(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            changeActivity()
        }
        return true
    }
}
