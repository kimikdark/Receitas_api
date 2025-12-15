package com.example.receitas.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.receitas.databinding.ItemMealBinding
import com.example.receitas.models.MealSummary

class MealListAdapter(private val onItemClicked: (String) -> Unit) :
    ListAdapter<MealSummary, MealListAdapter.MealViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
        holder.itemView.setOnClickListener { onItemClicked(meal.idMeal) }
    }

    class MealViewHolder(private val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: MealSummary) {
            binding.mealName.text = meal.strMeal
            binding.mealImage.load(meal.strMealThumb) {
                crossfade(true)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MealSummary>() {
            override fun areItemsTheSame(oldItem: MealSummary, newItem: MealSummary): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: MealSummary, newItem: MealSummary): Boolean {
                return oldItem == newItem
            }
        }
    }
}
