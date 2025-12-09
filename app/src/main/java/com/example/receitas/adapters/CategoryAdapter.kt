package com.example.receitas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.receitas.databinding.ItemCategoryBinding
import com.example.receitas.models.Category
import com.squareup.picasso.Picasso

class CategoryAdapter(private var categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // Armazena uma referência para o ViewBinding de cada item.
    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    // Chamado pelo RecyclerView para criar um novo ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // "Infla" o layout do item usando ViewBinding.
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    // Chamado pelo RecyclerView para associar dados a um ViewHolder existente.
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.categoryName.text = category.strCategory

        // Usa o Picasso para carregar a imagem a partir do URL.
        Picasso.get()
            .load(category.strCategoryThumb)
            .into(holder.binding.categoryThumb)
    }

    // Retorna o número total de itens na lista.
    override fun getItemCount() = categories.size

    /**
     * Atualiza a lista de categorias e notifica o adapter para redesenhar a UI.
     */
    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
