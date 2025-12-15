package com.example.receitas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.receitas.models.Category
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getCategories()
                if (response.isSuccessful) {
                    _categories.value = response.body()?.categories ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            }
            _isLoading.value = false
        }
    }
}
