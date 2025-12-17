package com.example.receitas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.models.Meal
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class RandomRecipeViewModel : ViewModel() {

    private val _randomMeal = MutableLiveData<Meal?>()
    val randomMeal: LiveData<Meal?> get() = _randomMeal

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchRandomMeal() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitClient.api.getRandomMeal()
                if (response.isSuccessful) {
                    _randomMeal.value = response.body()?.meals?.firstOrNull()
                } else {
                    _error.value = "Erro ao carregar receita: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRandomMealByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Primeiro busca receitas da categoria
                val categoryResponse = RetrofitClient.api.getMealsByCategory(category)
                if (categoryResponse.isSuccessful) {
                    val meals = categoryResponse.body()?.meals
                    if (meals != null && meals.isNotEmpty()) {
                        // Escolhe uma receita aleatória da lista
                        val randomMealSummary = meals.random()
                        // Busca os detalhes completos
                        val detailsResponse = RetrofitClient.api.getMealDetails(randomMealSummary.idMeal)
                        if (detailsResponse.isSuccessful) {
                            _randomMeal.value = detailsResponse.body()?.meals?.firstOrNull()
                        } else {
                            _error.value = "Erro ao carregar detalhes: ${detailsResponse.code()}"
                        }
                    } else {
                        _error.value = "Nenhuma receita encontrada nesta categoria"
                    }
                } else {
                    _error.value = "Erro ao buscar categoria: ${categoryResponse.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
