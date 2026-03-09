package com.guyanne.dietplanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guyanne.dietplanner.domain.model.Meal
import com.guyanne.dietplanner.domain.model.MealType
import com.guyanne.dietplanner.viewmodel.MealPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    onAddMeal: () -> Unit,
    onNutrition: () -> Unit,
    onShopping: () -> Unit,
    onSettings: () -> Unit,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val meals by viewModel.meals.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { 
                    Column {
                        Text("Meal Plan", fontWeight = FontWeight.ExtraBold)
                        Text(
                            text = "Eat healthy, feel great",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = onNutrition) {
                        Icon(Icons.Default.BarChart, contentDescription = "Stats")
                    }
                    IconButton(onClick = onShopping) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping")
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddMeal,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                expanded = scrollBehavior.state.collapsedFraction < 0.5f,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Meal") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Day selector with better design
            DaySelector(
                days = viewModel.daysOfWeek,
                selectedDay = selectedDay,
                onDaySelected = { viewModel.selectDay(it) }
            )

            if (meals.isEmpty()) {
                EmptyMealState(selectedDay)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val groupedMeals = meals.groupBy { it.mealType }
                    
                    MealType.entries.forEach { type ->
                        val typeMeals = groupedMeals[type] ?: emptyList()
                        if (typeMeals.isNotEmpty()) {
                            item(key = "header_${type.name}") {
                                Text(
                                    text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                            items(typeMeals, key = { it.id }) { meal ->
                                MealCard(
                                    meal = meal, 
                                    onDelete = { viewModel.deleteMeal(meal) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DaySelector(
    days: List<String>,
    selectedDay: String,
    onDaySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        days.forEach { day ->
            val isSelected = day == selectedDay
            val backgroundColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                label = "dayBg"
            )
            val contentColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "dayContent"
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor)
                    .clickable { onDaySelected(day) }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day.take(3).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun EmptyMealState(day: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "No meals for $day",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "You haven't planned any meals for this day yet. Time to get creative!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun MealCard(meal: Meal, onDelete: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(animationSpec = tween(400)),
        exit = shrinkVertically() + fadeOut()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon representation
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            when(meal.mealType) {
                                MealType.BREAKFAST -> "🍳"
                                MealType.LUNCH -> "🥗"
                                MealType.DINNER -> "🍲"
                                MealType.SNACK -> "🍎"
                            },
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        meal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (meal.description.isNotEmpty()) {
                        Text(
                            meal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        NutritionBadge("🔥 ${meal.calories} kcal", Color(0xFFFF7043))
                        NutritionBadge("P: ${meal.protein.toInt()}g", Color(0xFF42A5F5))
                        NutritionBadge("C: ${meal.carbs.toInt()}g", Color(0xFFFFCA28))
                        NutritionBadge("F: ${meal.fat.toInt()}g", Color(0xFFAB47BC))
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NutritionBadge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
