package com.example.aswitch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aswitch.Recipe
import com.example.aswitch.R
import kotlinx.android.synthetic.main.item_recipe.view.*

class FindRecipeAdapter(
    private var recipes: MutableList<Recipe>
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
            tvCost.text = curRecipe.cost
            setOnClickListener {

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
}