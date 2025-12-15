package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.receitas.adapters.MealListAdapter
import com.example.receitas.databinding.FragmentMealListBinding
import com.example.receitas.viewmodels.MealListViewModel

class MealListFragment : Fragment() {

    private var _binding: FragmentMealListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealListViewModel by viewModels()
    private val args: MealListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mealListAdapter = MealListAdapter { mealId ->
            val action = MealListFragmentDirections.actionMealListFragmentToRecipeDetailFragment(mealId)
            findNavController().navigate(action)
        }

        binding.recyclerViewMeals.apply {
            layoutManager = GridLayoutManager(context, 1) // Alterado para 1 coluna
            adapter = mealListAdapter
        }

        viewModel.fetchMealsByCategory(args.categoryName)

        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            mealListAdapter.submitList(meals)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
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
