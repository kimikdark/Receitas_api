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
        // Persiste a remoção no repositório
        repository.removeItem(item)

        // Atualiza a lista na UI de forma eficiente, removendo apenas um item
        val currentList = _shoppingListItems.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList.removeAt(index)
            _shoppingListItems.value = currentList
        } else {
            // Se, por algum motivo, o item não for encontrado, recarrega a lista
            loadItems()
        }
    }

    fun clearPurchasedItems() {
        repository.clearPurchasedItems()
        // Atualiza a lista na UI para remover os itens comprados
        val currentList = _shoppingListItems.value?.toMutableList() ?: return
        currentList.removeAll { it.isChecked }
        _shoppingListItems.value = currentList
    }
}
