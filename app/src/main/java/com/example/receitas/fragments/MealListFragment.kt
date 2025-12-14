package com.example.receitas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.receitas.databinding.FragmentMealListBinding

class MealListFragment : Fragment() {

    private var _binding: FragmentMealListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // A lógica para buscar as receitas será adicionada aqui.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
