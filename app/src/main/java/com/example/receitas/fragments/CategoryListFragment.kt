package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.receitas.adapters.CategoryAdapter
import com.example.receitas.databinding.FragmentCategoryListBinding
import com.example.receitas.services.RetrofitClient
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment() {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchCategories()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoryAdapter
        }
    }

    private fun fetchCategories() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getCategories()
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val categories = response.body()?.categories ?: emptyList()
                    categoryAdapter.updateCategories(categories)
                } else {
                    showError("Falha ao carregar categorias.")
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                showError("Erro de conexão: ${e.message}")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
