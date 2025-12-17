package com.example.receitas.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.data.AppDatabase
import com.example.receitas.models.FavoriteRecipe
import com.example.receitas.models.Meal
import com.example.receitas.repositories.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoriteRepository
    val allFavorites: Flow<List<FavoriteRecipe>>

    init {
        val favoriteDao = AppDatabase.getDatabase(application).favoriteRecipeDao()
        repository = FavoriteRepository(favoriteDao)
        allFavorites = repository.allFavorites
    }

    fun isFavorite(mealId: String): Flow<Boolean> {
        return repository.isFavorite(mealId)
    }

    fun addFavorite(meal: Meal) = viewModelScope.launch {
        repository.addFavorite(meal)
    }

    fun removeFavorite(mealId: String) = viewModelScope.launch {
        repository.removeFavorite(mealId)
    }

    fun toggleFavorite(meal: Meal, isFavorite: Boolean) = viewModelScope.launch {
        if (isFavorite) {
            repository.removeFavorite(meal.idMeal)
        } else {
            repository.addFavorite(meal)
        }
    }

    fun clearAllFavorites() = viewModelScope.launch {
        repository.clearAllFavorites()
    }
}
