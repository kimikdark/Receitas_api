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

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeDetailViewModel by activityViewModels()
    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels()
    private val args: RecipeDetailFragmentArgs by navArgs()

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
                binding.recipeImage.load(meal.strMealThumb) {
                    crossfade(true)
                }
                binding.recipeName.text = meal.strMeal
                binding.recipeArea.text = meal.strArea
                binding.recipeInstructions.text = meal.strInstructions

                // Limpa e preenche a lista de ingredientes
                binding.ingredientsContainer.removeAllViews()
                val ingredients = meal.getIngredients()
                if (ingredients.isNotEmpty()) {
                    binding.ingredientsTitle.isVisible = true
                    ingredients.forEach { (ingredient, measure) ->
                        val textView = TextView(context).apply {
                            text = "- $ingredient ($measure)"
                            textSize = 16f
                        }
                        binding.ingredientsContainer.addView(textView)
                    }
                } else {
                    binding.ingredientsTitle.isVisible = false
                }

                binding.addToShoppingListButton.setOnClickListener {
                    val itemsToAdd = ingredients.map { (ingredient, measure) ->
                        ShoppingListItem(
                            id = System.currentTimeMillis(), // Unique ID
                            ingredient = ingredient,
                            measure = measure
                        )
                    }
                    shoppingListViewModel.addItems(itemsToAdd)
                    Toast.makeText(context, "Ingredientes adicionados à lista!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            // TODO: Add a ProgressBar to the layout and manage its visibility
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
