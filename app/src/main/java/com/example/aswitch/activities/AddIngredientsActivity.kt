package com.example.aswitch.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.AddIngredientAdapter
import com.example.aswitch.dialogs.QuantityDialog
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_add_ingredients.*
import java.io.Serializable
import kotlin.properties.Delegates


class AddIngredientsActivity : AppCompatActivity(), QuantityDialog.ExampleDialogListener {

    private lateinit var adapter: AddIngredientAdapter
    private lateinit var extraIngredients: MutableList<Ingredient>
    private lateinit var extraKeyWords: MutableList<String>
    private lateinit var extraRecipe: Recipe
    private var ifUpadte = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        extraIngredients = intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        extraKeyWords = intent.getStringArrayListExtra("extra_keyWords") as ArrayList<String>
        if(intent.extras?.get("extra_recipe") != null) {
            extraRecipe = intent.extras?.get("extra_recipe") as Recipe
        }
        ifUpadte = intent.getBooleanExtra("extra_if_update", false)

        val ingredients = fetchIngredientsFromDB()

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

    private fun fetchIngredientsFromDB(): MutableList<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        val db = DBHelper(this, null)
        val cursor = db.getTitle()
        cursor!!.moveToFirst()
        while (cursor.moveToNext()) {
            ingredients.add(
                Ingredient(
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.INGREDIENTS_TITLE_COL)),
                    ""
                )
            )
        }
        cursor.moveToLast()
        ingredients.add(
            Ingredient(
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.INGREDIENTS_TITLE_COL)),
                ""
            )
        )
        return ingredients
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
        extraIngredients.add(Ingredient(title,quantity))
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

    override fun addIngredientToDB(title: String) {
        val dbHelper = DBHelper(this, null)
        dbHelper.addIngredient(title)
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
