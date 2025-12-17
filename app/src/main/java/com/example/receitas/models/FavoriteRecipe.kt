package com.example.receitas.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipe(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strArea: String?,
    val strMealThumb: String?,
    val strInstructions: String?,
    val ingredients: String, // JSON string of ingredients
    val measures: String, // JSON string of measures
    val addedAt: Long = System.currentTimeMillis()
)
