package com.guyanne.dietplanner.domain.model

data class Meal(
    val id: Long = 0,
    val name: String,
    val description: String,
    val ingredients: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val mealType: MealType = MealType.LUNCH,
    val dayOfWeek: String = "Monday"
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

data class ShoppingItem(
    val id: Long = 0,
    val name: String,
    val quantity: String,
    val isChecked: Boolean = false
)

data class NutritionSummary(
    val totalCalories: Int,
    val totalProtein: Float,
    val totalCarbs: Float,
    val totalFat: Float,
    val dailyGoal: Int = 2000
)
