package com.example.receitas.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ingredient: String,
    val measure: String,
    var isChecked: Boolean = false
)
