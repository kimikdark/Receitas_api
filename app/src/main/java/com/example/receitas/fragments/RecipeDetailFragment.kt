package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.receitas.databinding.FragmentRecipeDetailBinding
import com.example.receitas.models.ShoppingListItem
import com.example.receitas.viewmodels.RecipeDetailViewModel
import com.example.receitas.viewmodels.ShoppingListViewModel
import java.text.DecimalFormat

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeDetailViewModel by activityViewModels()
    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

    private var originalIngredients: Map<String, String> = emptyMap()
    private val originalPortions = 2 // Vamos assumir 2 porções como base
    private var currentPortions = originalPortions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchRecipeDetails(args.mealId)

        viewModel.recipeDetails.observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                binding.recipeImage.load(meal.strMealThumb) { crossfade(true) }
                binding.recipeName.text = meal.strMeal
                binding.recipeArea.text = meal.strArea
                binding.recipeInstructions.text = meal.strInstructions

                originalIngredients = meal.getIngredients().toMap()
                updateIngredientsUI()

                binding.addToShoppingListButton.setOnClickListener {
                    val itemsToAdd = originalIngredients.map { (ingredient, measure) ->
                        ShoppingListItem(
                            id = 0, // ID será gerado pelo Repository
                            ingredient = ingredient,
                            measure = calculateNewMeasure(measure, currentPortions.toDouble() / originalPortions)
                        )
                    }
                    shoppingListViewModel.addItems(itemsToAdd)
                    Toast.makeText(context, "Ingredientes adicionados à lista!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.increasePortionsButton.setOnClickListener {
            currentPortions++
            updateIngredientsUI()
        }

        binding.decreasePortionsButton.setOnClickListener {
            if (currentPortions > 1) {
                currentPortions--
                updateIngredientsUI()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { /* ... */ }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateIngredientsUI() {
        binding.portionsTextView.text = "$currentPortions porções"
        binding.ingredientsContainer.removeAllViews()
        val multiplier = currentPortions.toDouble() / originalPortions

        if (originalIngredients.isNotEmpty()) {
            binding.ingredientsTitle.isVisible = true
            originalIngredients.forEach { (ingredient, measure) ->
                val newMeasure = calculateNewMeasure(measure, multiplier)
                val textView = TextView(context).apply {
                    text = "- $ingredient ($newMeasure)"
                    textSize = 16f
                }
                binding.ingredientsContainer.addView(textView)
            }
        } else {
            binding.ingredientsTitle.isVisible = false
        }
    }

    private fun calculateNewMeasure(originalMeasure: String, multiplier: Double): String {
        val numberPart = originalMeasure.filter { it.isDigit() || it == '.' || it == '/' || it.isWhitespace() }
        val unitPart = originalMeasure.replace(numberPart, "").trim()

        if (numberPart.isBlank()) return originalMeasure // Se não houver número, retorna a medida original

        try {
            val originalValue = parseFraction(numberPart.trim())
            val newValue = originalValue * multiplier
            val formatter = DecimalFormat("#.##") // Formata para no máximo 2 casas decimais
            return "${formatter.format(newValue)} $unitPart"
        } catch (e: NumberFormatException) {
            return originalMeasure // Em caso de erro, retorna o original
        }
    }

    private fun parseFraction(fraction: String): Double {
        return if (fraction.contains('/')) {
            val parts = fraction.split('/')
            parts[0].toDouble() / parts[1].toDouble()
        } else {
            fraction.toDouble()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
