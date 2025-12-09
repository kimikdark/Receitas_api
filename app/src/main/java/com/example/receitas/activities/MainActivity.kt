package com.example.receitas.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.receitas.adapters.CategoryAdapter
import com.example.receitas.databinding.ActivityMainBinding
import com.example.receitas.models.Category
import com.example.receitas.models.CategoryResponse
import com.example.receitas.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCategories()
    }

    /**
     * Configura o RecyclerView com um GridLayoutManager e o Adapter.
     */
    private fun setupRecyclerView() {
        // Inicializa o adapter com uma lista vazia.
        categoryAdapter = CategoryAdapter(emptyList())
        binding.recyclerView.apply {
            // Mostra os itens numa grelha com 2 colunas.
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = categoryAdapter
        }
    }

    /**
     * Busca as categorias da API usando Retrofit.
     */
    private fun fetchCategories() {
        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val categories = response.body()?.categories ?: emptyList()
                    categoryAdapter.updateCategories(categories)
                } else {
                    showError("Falha ao carregar categorias.")
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                showError("Erro de conexão: ${t.message}")
            }
        })
    }

    /**
     * Mostra uma mensagem de erro curta (Toast).
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
