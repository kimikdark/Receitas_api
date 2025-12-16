package com.example.receitas.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.receitas.models.ShoppingListItem
import com.example.receitas.repositories.ShoppingListRepository

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShoppingListRepository(application)

    private val _shoppingListItems = MutableLiveData<List<ShoppingListItem>>()
    val shoppingListItems: LiveData<List<ShoppingListItem>> = _shoppingListItems

    init {
        loadItems()
    }

    private fun loadItems() {
        _shoppingListItems.value = repository.getAllItems()
    }

    fun addItems(items: List<ShoppingListItem>) {
        repository.addItems(items)
        loadItems()
    }

    fun updateItem(item: ShoppingListItem) {
        repository.updateItem(item)
        val currentList = _shoppingListItems.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList[index] = item
            _shoppingListItems.value = currentList
        } else {
            loadItems()
        }
    }

    fun removeItem(item: ShoppingListItem) {
        repository.removeItem(item)
        val currentList = _shoppingListItems.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == item.id }
        _shoppingListItems.value = currentList
    }

    fun clearPurchasedItems() {
        repository.clearPurchasedItems()
        loadItems()
    }
}
