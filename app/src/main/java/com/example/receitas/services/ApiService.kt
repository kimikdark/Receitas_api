package com.example.receitas.services

import com.example.receitas.models.CategoryResponse
import com.example.receitas.models.MealResponse
import com.example.receitas.models.MealSummaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Obtém a lista de todas as categorias de refeições.
     */
    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

    /**
     * Obtém a lista de refeições para uma categoria específica.
     * @param category O nome da categoria (ex: "Seafood").
     */
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): Response<MealSummaryResponse>

    /**
     * Obtém os detalhes completos de uma refeição pelo seu ID.
     * @param id O ID da refeição (ex: "52772").
     */
    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") id: String): Response<MealResponse>

    /**
     * Obtém uma única refeição aleatória.
     */
    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealResponse>

    /**
     * Procura refeições pelo nome.
     * @param query O termo de pesquisa (ex: "Arrabiata").
     */
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): Response<MealResponse>
}
