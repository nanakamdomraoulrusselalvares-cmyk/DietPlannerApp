package com.guyanne.dietplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guyanne.dietplanner.ui.screens.*
import com.guyanne.dietplanner.viewmodel.LoginViewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object MealPlan : Screen("meal_plan")
    data object AddMeal : Screen("add_meal")
    data object NutritionStats : Screen("nutrition_stats")
    data object ShoppingList : Screen("shopping_list")
    data object Settings : Screen("settings")
}

@Composable
fun DietPlannerNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            val vm: LoginViewModel = hiltViewModel()
            val isLoggedIn by vm.isLoggedIn.collectAsState()
            
            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    navController.navigate(Screen.MealPlan.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            LoginScreen(viewModel = vm)
        }
        composable(Screen.MealPlan.route) {
            MealPlanScreen(
                onAddMeal = { navController.navigate(Screen.AddMeal.route) },
                onNutrition = { navController.navigate(Screen.NutritionStats.route) },
                onShopping = { navController.navigate(Screen.ShoppingList.route) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.AddMeal.route) {
            AddMealScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.NutritionStats.route) {
            NutritionStatsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ShoppingList.route) {
            ShoppingListScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
