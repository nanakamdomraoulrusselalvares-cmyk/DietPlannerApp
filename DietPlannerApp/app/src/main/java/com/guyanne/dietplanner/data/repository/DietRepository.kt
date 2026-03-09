package com.guyanne.dietplanner.data.repository

import com.guyanne.dietplanner.data.local.dao.MealDao
import com.guyanne.dietplanner.data.local.dao.ShoppingItemDao
import com.guyanne.dietplanner.data.local.entities.MealEntity
import com.guyanne.dietplanner.data.local.entities.ShoppingItemEntity
import com.guyanne.dietplanner.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DietRepository @Inject constructor(
    private val mealDao: MealDao,
    private val shoppingItemDao: ShoppingItemDao
) {
    // Meals
    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals().map { list ->
        list.map { it.toDomain() }
    }

    fun getMealsByDay(day: String): Flow<List<Meal>> = mealDao.getMealsByDay(day).map { list ->
        list.map { it.toDomain() }
    }

    suspend fun insertMeal(meal: Meal) = mealDao.insertMeal(meal.toEntity())

    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal.toEntity())

    // Shopping
    fun getAllShoppingItems(): Flow<List<ShoppingItem>> =
        shoppingItemDao.getAllItems().map { list -> list.map { it.toDomain() } }

    suspend fun insertShoppingItem(item: ShoppingItem) =
        shoppingItemDao.insertItem(item.toEntity())

    suspend fun updateShoppingItem(item: ShoppingItem) =
        shoppingItemDao.updateItem(item.toEntity())

    suspend fun deleteShoppingItem(item: ShoppingItem) =
        shoppingItemDao.deleteItem(item.toEntity())

    suspend fun clearShoppingList() = shoppingItemDao.deleteAllItems()

    // Mock seed data
    fun getMockMeals(): List<Meal> = listOf(
        Meal(name = "Oatmeal & Berries", description = "Wholesome breakfast bowl", ingredients = "Oats, blueberries, honey, almond milk", calories = 320, protein = 12f, carbs = 58f, fat = 6f, mealType = MealType.BREAKFAST, dayOfWeek = "Monday"),
        Meal(name = "Grilled Chicken Salad", description = "Light & protein-rich lunch", ingredients = "Chicken breast, romaine, tomato, cucumber, olive oil", calories = 450, protein = 42f, carbs = 18f, fat = 22f, mealType = MealType.LUNCH, dayOfWeek = "Monday"),
        Meal(name = "Salmon & Quinoa", description = "Omega-3 rich dinner", ingredients = "Salmon fillet, quinoa, broccoli, lemon", calories = 580, protein = 48f, carbs = 45f, fat = 18f, mealType = MealType.DINNER, dayOfWeek = "Monday"),
        Meal(name = "Greek Yogurt", description = "Afternoon snack", ingredients = "Greek yogurt, granola, honey", calories = 210, protein = 15f, carbs = 28f, fat = 4f, mealType = MealType.SNACK, dayOfWeek = "Monday"),
        Meal(name = "Avocado Toast", description = "Nutrient-dense breakfast", ingredients = "Whole grain bread, avocado, eggs, chili flakes", calories = 380, protein = 16f, carbs = 34f, fat = 22f, mealType = MealType.BREAKFAST, dayOfWeek = "Tuesday"),
        Meal(name = "Turkey Wrap", description = "Lean protein lunch wrap", ingredients = "Turkey slices, whole wheat tortilla, lettuce, mustard", calories = 420, protein = 35f, carbs = 40f, fat = 10f, mealType = MealType.LUNCH, dayOfWeek = "Tuesday"),
        Meal(name = "Beef Stir-Fry", description = "High-protein dinner", ingredients = "Lean beef, mixed vegetables, soy sauce, brown rice", calories = 620, protein = 45f, carbs = 65f, fat = 16f, mealType = MealType.DINNER, dayOfWeek = "Tuesday"),
        Meal(name = "Mixed Nuts", description = "Healthy snack", ingredients = "Almonds, cashews, walnuts", calories = 180, protein = 6f, carbs = 8f, fat = 16f, mealType = MealType.SNACK, dayOfWeek = "Tuesday"),
        Meal(name = "Banana Smoothie", description = "Quick breakfast", ingredients = "Banana, protein powder, almond milk, peanut butter", calories = 400, protein = 28f, carbs = 50f, fat = 10f, mealType = MealType.BREAKFAST, dayOfWeek = "Wednesday"),
        Meal(name = "Lentil Soup", description = "Fiber-rich lunch", ingredients = "Red lentils, carrots, onion, cumin, vegetable broth", calories = 380, protein = 22f, carbs = 60f, fat = 6f, mealType = MealType.LUNCH, dayOfWeek = "Wednesday"),
    )

    fun getMockShoppingItems(): List<ShoppingItem> = listOf(
        ShoppingItem(name = "Chicken Breast", quantity = "500g"),
        ShoppingItem(name = "Salmon Fillet", quantity = "2 pieces"),
        ShoppingItem(name = "Oats", quantity = "1 kg"),
        ShoppingItem(name = "Greek Yogurt", quantity = "3 cups"),
        ShoppingItem(name = "Quinoa", quantity = "500g"),
        ShoppingItem(name = "Broccoli", quantity = "2 heads"),
        ShoppingItem(name = "Mixed Nuts", quantity = "200g"),
        ShoppingItem(name = "Avocado", quantity = "4 pieces"),
        ShoppingItem(name = "Whole Grain Bread", quantity = "1 loaf"),
        ShoppingItem(name = "Eggs", quantity = "12"),
        ShoppingItem(name = "Brown Rice", quantity = "1 kg"),
        ShoppingItem(name = "Almond Milk", quantity = "1 L"),
    )
}

private fun MealEntity.toDomain() = Meal(
    id = id, name = name, description = description, ingredients = ingredients,
    calories = calories, protein = protein, carbs = carbs, fat = fat,
    mealType = try { MealType.valueOf(mealType) } catch (e: Exception) { MealType.LUNCH },
    dayOfWeek = dayOfWeek
)

private fun Meal.toEntity() = MealEntity(
    id = id, name = name, description = description, ingredients = ingredients,
    calories = calories, protein = protein, carbs = carbs, fat = fat,
    mealType = mealType.name, dayOfWeek = dayOfWeek
)

private fun ShoppingItemEntity.toDomain() = ShoppingItem(id = id, name = name, quantity = quantity, isChecked = isChecked)

private fun ShoppingItem.toEntity() = ShoppingItemEntity(id = id, name = name, quantity = quantity, isChecked = isChecked)
