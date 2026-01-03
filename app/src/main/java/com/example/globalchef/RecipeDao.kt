package com.example.globalchef

import androidx.room.*

@Dao
interface RecipeDao {

    // Insert a new recipe into the database
    @Insert
    suspend fun insert(recipe: Recipe)

    // Get all recipes of a specific cuisine (Italian, American, Desi, etc.)
    @Query("SELECT * FROM recipes WHERE cuisine = :cuisine ORDER BY id DESC")
    suspend fun getByCuisine(cuisine: String): List<Recipe>

    // Get all recipes regardless of cuisine
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    suspend fun getAll(): List<Recipe>

    // Delete a recipe by its ID
    @Delete
    suspend fun delete(recipe: Recipe)

}
