package com.example.receitas.models

data class ShoppingListItem(
    val id: Long,
    val ingredient: String,
    val measure: String,
    var isChecked: Boolean = false
)
