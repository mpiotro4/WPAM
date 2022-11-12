package com.example.aswitch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import kotlinx.android.synthetic.main.item_ingredient_add.view.*

class AddIngredientAdapter (
    private var ingredients: MutableList<Ingredient>
) : RecyclerView.Adapter<AddIngredientAdapter.IngredientViewHolder>() {
    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIngredientAdapter.IngredientViewHolder {
        return AddIngredientAdapter.IngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ingredient_add,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddIngredientAdapter.IngredientViewHolder, position: Int) {
        val curTodo = ingredients[position]
        holder.itemView.apply {
            tvIngredient.text = curTodo.title
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setList(ingredients: MutableList<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}