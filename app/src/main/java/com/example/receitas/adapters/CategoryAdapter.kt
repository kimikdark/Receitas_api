package com.example.receitas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.receitas.R
import com.example.receitas.databinding.ItemCategoryBinding
import com.example.receitas.models.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryName.text = category.strCategory

        // Usa a Coil para carregar a imagem a partir do URL.
        holder.binding.categoryThumb.load(category.strCategoryThumb) {
            crossfade(true)
            placeholder(R.drawable.gradient_overlay)
            error(R.drawable.gradient_overlay)
        }

        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    override fun getItemCount() = categories.size

    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
