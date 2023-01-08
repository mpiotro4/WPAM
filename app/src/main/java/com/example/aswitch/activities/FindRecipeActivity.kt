package com.example.aswitch.activities
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.FindRecipeAdapter
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_find_recipe.*
import java.io.Serializable

class FindRecipeActivity : AppCompatActivity() {
    private lateinit var adapter: FindRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_recipe)

        val recipes = fetchRecipesFromDB()

        adapter = FindRecipeAdapter(recipes, this)
        rvRecipes.adapter = adapter
        rvRecipes.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchRecipesFromDB (): MutableList<Recipe> {
        val recipes = mutableListOf<Recipe>()
        val db = DBHelper(this, null)
        val cursor = db.getRecipes()
        cursor!!.moveToFirst()
        try {
            var recipeId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_ID_COL))
            var keywords = db.getRecipeKeywords(recipeId)
            recipes.add(
                Recipe(
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_ID_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TITLE_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_COST_COL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TIME_COL)),
                    keywords
                )
            )
            while (cursor.moveToNext()) {
                recipeId = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_ID_COL))
                keywords = db.getRecipeKeywords(recipeId)
                recipes.add(
                    Recipe(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_ID_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TITLE_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_COST_COL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.RECIPES_TIME_COL)),
                        keywords
                    )
                )
            }
            db.close()
            return recipes
        } catch (e: Exception) {
            return recipes
        }
    }

    fun startReadActivity(curRecipe: Recipe) {
        Intent(this, ReadRecipeActivity::class.java).also {
            it.putExtra("extra_recipe", curRecipe as Serializable)
            startActivity(it)
        }
    }
}