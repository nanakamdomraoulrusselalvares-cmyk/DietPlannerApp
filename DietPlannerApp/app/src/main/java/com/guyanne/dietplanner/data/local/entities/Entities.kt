package com.guyanne.dietplanner.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val ingredients: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val mealType: String,
    val dayOfWeek: String
)

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val quantity: String,
    val isChecked: Boolean = false
)
