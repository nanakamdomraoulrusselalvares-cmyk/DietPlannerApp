package com.guyanne.dietplanner.data.local.dao

import androidx.room.*
import com.guyanne.dietplanner.data.local.entities.MealEntity
import com.guyanne.dietplanner.data.local.entities.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY id ASC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE dayOfWeek = :day")
    fun getMealsByDay(day: String): Flow<List<MealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()
}

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items ORDER BY id ASC")
    fun getAllItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShoppingItemEntity): Long

    @Update
    suspend fun updateItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items")
    suspend fun deleteAllItems()
}
