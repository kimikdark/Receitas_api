package com.example.receitas.repositories

import android.content.Context
import com.example.receitas.models.ShoppingListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShoppingListRepository(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("shopping_list_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var nextId: Long = sharedPreferences.getLong("next_id", 1L)

    private fun saveList(list: List<ShoppingListItem>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("shopping_list", json).apply()
    }

    private fun getNextId(): Long {
        val id = nextId++
        sharedPreferences.edit().putLong("next_id", nextId).apply()
        return id
    }

    fun getAllItems(): List<ShoppingListItem> {
        val json = sharedPreferences.getString("shopping_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<ShoppingListItem>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addItems(items: List<ShoppingListItem>) {
        val currentList = getAllItems().toMutableList()
        items.forEach { newItem ->
            if (currentList.none { it.ingredient.equals(newItem.ingredient, ignoreCase = true) }) {
                currentList.add(newItem.copy(id = getNextId()))
            }
        }
        saveList(currentList.sortedBy { it.ingredient })
    }

    fun updateItem(item: ShoppingListItem) {
        val currentList = getAllItems().toMutableList()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList[index] = item
            saveList(currentList)
        }
    }

    fun removeItem(item: ShoppingListItem) {
        val currentList = getAllItems().toMutableList()
        val index = currentList.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentList.removeAt(index)
            saveList(currentList)
        }
    }

    fun clearPurchasedItems() {
        val currentList = getAllItems()
        val updatedList = currentList.filter { !it.isChecked }
        saveList(updatedList)
    }
}
