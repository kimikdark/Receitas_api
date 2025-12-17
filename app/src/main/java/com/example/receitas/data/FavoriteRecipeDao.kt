package com.example.receitas.data

import androidx.room.*
import com.example.receitas.models.FavoriteRecipe
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    
    @Query("SELECT * FROM favorite_recipes ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteRecipe>>
    
    @Query("SELECT * FROM favorite_recipes WHERE idMeal = :mealId")
    suspend fun getFavoriteById(mealId: String): FavoriteRecipe?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE idMeal = :mealId)")
    fun isFavorite(mealId: String): Flow<Boolean>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteRecipe)
    
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteRecipe)
    
    @Query("DELETE FROM favorite_recipes WHERE idMeal = :mealId")
    suspend fun deleteFavoriteById(mealId: String)
    
    @Query("DELETE FROM favorite_recipes")
    suspend fun deleteAllFavorites()
}
