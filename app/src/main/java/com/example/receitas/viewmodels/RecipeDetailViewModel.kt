package com.example.receitas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.models.Meal
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {

    private val _recipeDetails = MutableLiveData<Meal?>()
    val recipeDetails: LiveData<Meal?> = _recipeDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchRecipeDetails(mealId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getMealDetails(mealId)
                if (response.isSuccessful) {
                    _recipeDetails.value = response.body()?.meals?.firstOrNull()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load recipe details: ${e.message}"
            }
            _isLoading.value = false
        }
    }
}
