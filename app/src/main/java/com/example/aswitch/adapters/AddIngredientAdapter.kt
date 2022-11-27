package com.example.aswitch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import kotlinx.android.synthetic.main.item_ingredient_add.view.*
import kotlin.reflect.KFunction1

class AddIngredientAdapter(
    private var ingredients: MutableList<Ingredient>,
    private val openQuantityDialog: KFunction1<String, Unit>,
) : RecyclerView.Adapter<AddIngredientAdapter.IngredientViewHolder>() {
    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ingredient_add,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val curTodo = ingredients[position]
        holder.itemView.apply {
            tvIngredient.text = curTodo.title
            setOnClickListener {
                val title = getTitleAfterSearch(curTodo.title)
                openQuantityDialog(title)
                addIngredientFromSearchView(title)
            }
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setList(ingredients: MutableList<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    private fun getTitleAfterSearch(searchText: String): String {
        val titleMatcher = "\"(.+)\"".toRegex()
        return titleMatcher.find(searchText)?.groupValues?.getOrNull(1) ?: searchText
    }

    private fun addIngredientFromSearchView(searchText: String){
        val title = getTitleAfterSearch(searchText)
        if(title != searchText){
            ingredients.add(Ingredient(title,""))
            notifyDataSetChanged()
        }
    }
}