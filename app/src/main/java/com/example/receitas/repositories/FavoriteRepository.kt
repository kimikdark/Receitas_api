package com.example.receitas.repositories

import com.example.receitas.data.FavoriteRecipeDao
import com.example.receitas.models.FavoriteRecipe
import com.example.receitas.models.Meal
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteRecipeDao: FavoriteRecipeDao) {

    val allFavorites: Flow<List<FavoriteRecipe>> = favoriteRecipeDao.getAllFavorites()

    fun isFavorite(mealId: String): Flow<Boolean> {
        return favoriteRecipeDao.isFavorite(mealId)
    }

    suspend fun addFavorite(meal: Meal) {
        val gson = Gson()
        val ingredients = gson.toJson(meal.getIngredients().map { it.first })
        val measures = gson.toJson(meal.getIngredients().map { it.second })

        val favorite = FavoriteRecipe(
            idMeal = meal.idMeal,
            strMeal = meal.strMeal,
            strCategory = meal.strCategory,
            strArea = meal.strArea,
            strMealThumb = meal.strMealThumb,
            strInstructions = meal.strInstructions,
            ingredients = ingredients,
            measures = measures
        )
        favoriteRecipeDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(mealId: String) {
        favoriteRecipeDao.deleteFavoriteById(mealId)
    }

    suspend fun getFavoriteById(mealId: String): FavoriteRecipe? {
        return favoriteRecipeDao.getFavoriteById(mealId)
    }

    suspend fun clearAllFavorites() {
        favoriteRecipeDao.deleteAllFavorites()
    }
}
