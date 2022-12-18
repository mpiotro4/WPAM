package com.example.aswitch.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.FindRecipeAdapter
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_find_recipe.*

class FindRecipeActivity : AppCompatActivity() {
    private lateinit var adapter: FindRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_recipe)

        val recipes = fetchRecipesFromDB()

        adapter = FindRecipeAdapter(recipes)
        rvRecipes.adapter = adapter
        rvRecipes.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchRecipesFromDB (): MutableList<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = DBHelper(this, null)
        val cursor = db.getRecipes()
        cursor!!.moveToFirst()
        recipes.add(
            Recipe(
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TITLE_COL)),
                cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_COST_COL))
            )
        )
        while (cursor.moveToNext()) {
            recipes.add(
                Recipe(
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TITLE_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_COST_COL))
                )
            )
        }
        db.close()
        return recipes
    }
}