package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.receitas.R
import com.example.receitas.databinding.FragmentRandomRecipeBinding
import com.example.receitas.viewmodels.CategoryViewModel
import com.example.receitas.viewmodels.RandomRecipeViewModel

class RandomRecipeFragment : Fragment() {

    private var _binding: FragmentRandomRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RandomRecipeViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private var selectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryFilter()
        setupObservers()
        setupButtons()

        // Carregar primeira receita aleatória
        viewModel.fetchRandomMeal()
    }

    private fun setupCategoryFilter() {
        categoryViewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (!categories.isNullOrEmpty()) {
                val categoryNames = listOf("Todas") + categories.map { it.strCategory }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    categoryNames
                )
                binding.categoryFilterAutoComplete.setAdapter(adapter)

                binding.categoryFilterAutoComplete.setOnItemClickListener { _, _, position, _ ->
                    selectedCategory = if (position == 0) null else categoryNames[position]
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                binding.recipeCard.isVisible = true
                binding.emptyView.isVisible = false

                binding.recipeImage.load(meal.strMealThumb) { crossfade(true) }
                binding.recipeName.text = meal.strMeal
                binding.recipeCategory.text = meal.strCategory
                binding.recipeArea.text = meal.strArea

                binding.viewDetailsButton.setOnClickListener {
                    val action = RandomRecipeFragmentDirections
                        .actionRandomRecipeFragmentToRecipeDetailFragment(meal.idMeal)
                    findNavController().navigate(action)
                }
            } else {
                binding.recipeCard.isVisible = false
                binding.emptyView.isVisible = true
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.randomButton.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupButtons() {
        binding.randomButton.setOnClickListener {
            if (selectedCategory != null) {
                viewModel.fetchRandomMealByCategory(selectedCategory!!)
            } else {
                viewModel.fetchRandomMeal()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
