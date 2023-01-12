package com.example.aswitch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import kotlinx.android.synthetic.main.item_ingredient_read.view.*

class ReadIngredientAdapter (
    private val ingredients: MutableList<Ingredient>
) : RecyclerView.Adapter<ReadIngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ingredient_read,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val curTodo = ingredients[position]
        holder.itemView.apply {
            tvIngredient.text = curTodo.title
            tvQuantity.text = curTodo.quantity
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun addIngredient(ingredient: Ingredient) {
        ingredients.add(ingredient)
        notifyItemInserted(ingredients.size - 1)
    }
}