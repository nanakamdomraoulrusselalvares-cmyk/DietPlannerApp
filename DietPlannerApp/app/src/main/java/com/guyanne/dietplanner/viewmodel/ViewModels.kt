package com.guyanne.dietplanner.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guyanne.dietplanner.data.repository.DietRepository
import com.guyanne.dietplanner.domain.model.Meal
import com.guyanne.dietplanner.domain.model.MealType
import com.guyanne.dietplanner.domain.model.NutritionSummary
import com.guyanne.dietplanner.domain.model.ShoppingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

// ---- Login ViewModel ----
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _error.value = "Please enter username and password"
        } else {
            _error.value = null
            _isLoggedIn.value = true
        }
    }
}

// ---- Meal Plan ViewModel ----
@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    private val currentDay = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())
    private val _selectedDay = MutableStateFlow(if (currentDay in daysOfWeek) currentDay else "Monday")
    val selectedDay: StateFlow<String> = _selectedDay.asStateFlow()

    init {
        seedInitialData()
        observeMeals()
    }

    private fun seedInitialData() {
        viewModelScope.launch {
            repository.getAllMeals().first().let { dbMeals ->
                if (dbMeals.isEmpty()) {
                    repository.getMockMeals().forEach { repository.insertMeal(it) }
                }
            }
        }
    }

    private fun observeMeals() {
        repository.getAllMeals()
            .combine(_selectedDay) { meals, day ->
                meals.filter { it.dayOfWeek == day }
            }
            .onEach { _meals.value = it }
            .launchIn(viewModelScope)
    }

    fun selectDay(day: String) {
        _selectedDay.value = day
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch { repository.deleteMeal(meal) }
    }
}

// ---- Add Meal ViewModel ----
@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    private val _saved = MutableStateFlow(false)
    val saved: StateFlow<Boolean> = _saved.asStateFlow()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val mealTypes = MealType.entries.toList()

    fun saveMeal(
        name: String, description: String, ingredients: String,
        calories: String, protein: String, carbs: String, fat: String,
        mealType: MealType, dayOfWeek: String
    ) {
        if (name.isBlank()) return
        
        viewModelScope.launch {
            repository.insertMeal(
                Meal(
                    name = name, description = description, ingredients = ingredients,
                    calories = calories.toIntOrNull() ?: 0,
                    protein = protein.toFloatOrNull() ?: 0f,
                    carbs = carbs.toFloatOrNull() ?: 0f,
                    fat = fat.toFloatOrNull() ?: 0f,
                    mealType = mealType, dayOfWeek = dayOfWeek
                )
            )
            _saved.value = true
        }
    }
}

// ---- Nutrition Stats ViewModel ----
@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val repository: DietRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val dailyGoalKey = intPreferencesKey("daily_calorie_goal")

    private val _summary = MutableStateFlow(NutritionSummary(0, 0f, 0f, 0f))
    val summary: StateFlow<NutritionSummary> = _summary.asStateFlow()

    private val _weeklyCalories = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val weeklyCalories: StateFlow<List<Pair<String, Int>>> = _weeklyCalories.asStateFlow()

    private val _dailyGoal = MutableStateFlow(2000)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    private val days = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")

    init {
        viewModelScope.launch {
            dataStore.data.map { it[dailyGoalKey] ?: 2000 }.collect { goal ->
                _dailyGoal.value = goal
            }
        }
        
        val today = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())
        
        repository.getAllMeals()
            .combine(_dailyGoal) { meals, goal ->
                val todayMeals = meals.filter { it.dayOfWeek == today }
                val summary = NutritionSummary(
                    totalCalories = todayMeals.sumOf { it.calories },
                    totalProtein = todayMeals.sumOf { it.protein.toDouble() }.toFloat(),
                    totalCarbs = todayMeals.sumOf { it.carbs.toDouble() }.toFloat(),
                    totalFat = todayMeals.sumOf { it.fat.toDouble() }.toFloat(),
                    dailyGoal = goal
                )
                val weekly = days.map { day ->
                    day to meals.filter { it.dayOfWeek == day }.sumOf { it.calories }
                }
                summary to weekly
            }
            .onEach { (summary, weekly) ->
                _summary.value = summary
                _weeklyCalories.value = weekly
            }
            .launchIn(viewModelScope)
    }
}

// ---- Shopping List ViewModel ----
@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: DietRepository
) : ViewModel() {

    val items: StateFlow<List<ShoppingItem>> = repository.getAllShoppingItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.getAllShoppingItems().first().let { existing ->
                if (existing.isEmpty()) {
                    repository.getMockShoppingItems().forEach { repository.insertShoppingItem(it) }
                }
            }
        }
    }

    fun addItem(name: String, quantity: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.insertShoppingItem(ShoppingItem(name = name, quantity = quantity)) }
    }

    fun toggleItem(item: ShoppingItem) {
        viewModelScope.launch { repository.updateShoppingItem(item.copy(isChecked = !item.isChecked)) }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch { repository.deleteShoppingItem(item) }
    }

    fun clearAll() {
        viewModelScope.launch { repository.clearShoppingList() }
    }
}

// ---- Settings ViewModel ----
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val calorieGoalKey = intPreferencesKey("daily_calorie_goal")
    private val remindersKey = booleanPreferencesKey("meal_reminders")
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val calorieGoal: StateFlow<Int> = dataStore.data
        .map { it[calorieGoalKey] ?: 2000 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 2000)

    val remindersEnabled: StateFlow<Boolean> = dataStore.data
        .map { it[remindersKey] ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val darkMode: StateFlow<Boolean> = dataStore.data
        .map { it[darkModeKey] ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setCalorieGoal(goal: Int) {
        viewModelScope.launch {
            dataStore.edit { it[calorieGoalKey] = goal }
        }
    }

    fun toggleReminders() {
        viewModelScope.launch {
            dataStore.edit { it[remindersKey] = !(remindersEnabled.value) }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            dataStore.edit { it[darkModeKey] = !(darkMode.value) }
        }
    }
}
