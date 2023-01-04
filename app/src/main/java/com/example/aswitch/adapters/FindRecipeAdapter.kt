package com.example.aswitch.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aswitch.Recipe
import com.example.aswitch.R
import com.example.aswitch.activities.FindRecipeActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.item_recipe.view.*

class FindRecipeAdapter(
    private var recipes: MutableList<Recipe>,
    private val findRecipeActivity: FindRecipeActivity
) : RecyclerView.Adapter<FindRecipeAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recipe,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val curRecipe = recipes[position]
        holder.itemView.apply {
            tvTitle.text = curRecipe.title
            tvCost.text = "Koszt: " + curRecipe.cost + " zł"
            tvTime.text = "Czas: " + curRecipe.time + " min"

            val dbHelper = DBHelper(findRecipeActivity, null)
            val image = dbHelper.getRecipeImageById(curRecipe.id)
            val bmp = image?.let { it1 -> BitmapFactory.decodeByteArray(image, 0, it1.lastIndex) }
            imageView.setImageBitmap(bmp)


            cgKeyWords.removeAllViews()
            for(keyword in curRecipe.keyWords) {
                addChip(keyword, cgKeyWords)
            }
            setOnClickListener {
                findRecipeActivity.startReadActivity(curRecipe)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setList(recipes: MutableList<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    private fun addChip(txt: String, cgKeyWords: ChipGroup) {
        val chip = Chip(findRecipeActivity)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                findRecipeActivity,
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