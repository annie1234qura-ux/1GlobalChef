package com.example.globalchef

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var database: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        database = RecipeDatabase.getDatabase(this)

        // Get Recipe object from intent
        val recipe = intent.getSerializableExtra("recipe") as? Recipe

        if (recipe != null) {
            // Set recipe info
            findViewById<TextView>(R.id.tvRecipeTitle).text = recipe.title
            findViewById<TextView>(R.id.tvCuisine).text = "Cuisine: ${recipe.cuisine}"
            findViewById<TextView>(R.id.tvIngredients).text = recipe.ingredients
            findViewById<TextView>(R.id.tvSteps).text = recipe.steps

            // Delete button
            val btnDelete = findViewById<Button>(R.id.btnDeleteRecipe)
            btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    database.recipeDao().delete(recipe)
                    runOnUiThread {
                        Toast.makeText(
                            this@RecipeDetailActivity,
                            "Recipe deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close activity
                    }
                }
            }
        }
    }
}
