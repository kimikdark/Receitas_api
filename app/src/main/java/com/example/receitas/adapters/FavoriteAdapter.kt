package com.example.receitas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.receitas.databinding.ItemMealBinding
import com.example.receitas.models.FavoriteRecipe

class FavoriteAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<FavoriteRecipe, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.bind(favorite)
        holder.itemView.setOnClickListener { onItemClicked(favorite.idMeal) }
    }

    class FavoriteViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: FavoriteRecipe) {
            binding.mealName.text = favorite.strMeal
            binding.mealImage.load(favorite.strMealThumb) {
                crossfade(true)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FavoriteRecipe>() {
            override fun areItemsTheSame(oldItem: FavoriteRecipe, newItem: FavoriteRecipe): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: FavoriteRecipe, newItem: FavoriteRecipe): Boolean {
                return oldItem == newItem
            }
        }
    }
}
