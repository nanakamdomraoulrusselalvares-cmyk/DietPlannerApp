package com.guyanne.dietplanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guyanne.dietplanner.data.local.dao.MealDao
import com.guyanne.dietplanner.data.local.dao.ShoppingItemDao
import com.guyanne.dietplanner.data.local.entities.MealEntity
import com.guyanne.dietplanner.data.local.entities.ShoppingItemEntity

@Database(
    entities = [MealEntity::class, ShoppingItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DietDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}
