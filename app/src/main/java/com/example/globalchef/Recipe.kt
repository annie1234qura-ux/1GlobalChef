package com.example.globalchef

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable   // <--- add this

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val cuisine: String,        // e.g., Italian, American, Desi
    val ingredients: String,    // Comma-separated ingredients
    val steps: String,          // Step-by-step instructions
    val createdAt: String       // Date when recipe is saved
) : Serializable   // <--- add Serializable
