package com.example.globalchef

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeAdapter(
    private var recipes: List<Recipe>,
    private val onClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCuisine: TextView = itemView.findViewById(R.id.tvCuisine)
        val tvIngredients: TextView = itemView.findViewById(R.id.tvIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvTitle.text = recipe.title
        holder.tvCuisine.text = recipe.cuisine
        holder.tvIngredients.text = recipe.ingredients

        holder.itemView.setOnClickListener {
            onClick(recipe)  // <-- this is correct
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateData(newList: List<Recipe>) {
        recipes = newList
        notifyDataSetChanged()
    }
}
