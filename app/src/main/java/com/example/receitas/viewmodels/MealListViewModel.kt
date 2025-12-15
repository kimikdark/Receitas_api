package com.example.receitas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.models.MealSummary
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class MealListViewModel : ViewModel() {

    private val _meals = MutableLiveData<List<MealSummary>>()
    val meals: LiveData<List<MealSummary>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchMealsByCategory(categoryName: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getMealsByCategory(categoryName)
                if (response.isSuccessful) {
                    _meals.value = response.body()?.meals ?: emptyList()
                } else {
                     _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load meals: ${e.message}"
            }
            _isLoading.value = false
        }
    }
}
