package com.example.receitas.adapters

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.receitas.databinding.ItemShoppingListBinding
import com.example.receitas.models.ShoppingListItem

class ShoppingCartAdapter(private val onCheckboxClicked: (ShoppingListItem) -> Unit) :
    ListAdapter<ShoppingListItem, ShoppingCartAdapter.ViewHolder>(ShoppingListItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingListItem) {
            binding.ingredientTextView.text = "${item.ingredient} (${item.measure})"
            binding.itemCheckBox.isChecked = item.isChecked

            updateVisuals(binding.ingredientTextView, item.isChecked)

            binding.itemCheckBox.setOnClickListener {
                val updatedItem = item.copy(isChecked = binding.itemCheckBox.isChecked)
                onCheckboxClicked(updatedItem)
            }
        }

        private fun updateVisuals(textView: TextView, isChecked: Boolean) {
            if (isChecked) {
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                textView.setTextColor(Color.LTGRAY)
            } else {
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                textView.setTextColor(Color.BLACK) // Ou a sua cor de texto padrão
            }
        }
    }
}

class ShoppingListItemDiffCallback : DiffUtil.ItemCallback<ShoppingListItem>() {
    override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
        return oldItem == newItem
    }
}
