package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.receitas.adapters.ShoppingCartAdapter
import com.example.receitas.databinding.FragmentShoppingCartBinding
import com.example.receitas.viewmodels.ShoppingListViewModel

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShoppingCartAdapter { item ->
            viewModel.updateItem(item)
        }

        binding.shoppingListRecyclerView.adapter = adapter

        viewModel.shoppingListItems.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.clearListButton.setOnClickListener {
            viewModel.clearList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
