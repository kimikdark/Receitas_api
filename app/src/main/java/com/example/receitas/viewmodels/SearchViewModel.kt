package com.example.receitas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.models.MealSummary
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<MealSummary>>()
    val searchResults: LiveData<List<MealSummary>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun searchMeals(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.searchMeals(query)
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    // Converte a lista de Meal para uma lista de MealSummary
                    _searchResults.value = meals.map { meal ->
                        MealSummary(meal.idMeal, meal.strMeal, meal.strMealThumb ?: "")
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to perform search: ${e.message}"
            }
            _isLoading.value = false
        }
    }
}
