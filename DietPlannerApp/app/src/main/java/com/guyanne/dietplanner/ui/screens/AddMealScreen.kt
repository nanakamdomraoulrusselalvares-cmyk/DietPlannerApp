package com.guyanne.dietplanner.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SetMeal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guyanne.dietplanner.domain.model.MealType
import com.guyanne.dietplanner.viewmodel.AddMealViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(onBack: () -> Unit, viewModel: AddMealViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MealType.LUNCH) }
    var selectedDay by remember { mutableStateOf("Monday") }
    var typeExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val saved by viewModel.saved.collectAsState()
    
    LaunchedEffect(saved) {
        if (saved) { onBack() }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add New Meal", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.saveMeal(name, description, ingredients, calories, protein, carbs, fat, selectedType, selectedDay)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Check, contentDescription = null) },
                text = { Text("Save Meal") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header animation
            AnimatedAppearance(delay = 0) {
                Column {
                    Text(
                        "What's on the menu?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Fill in the details to track your nutrition",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Basic Info Section
            AnimatedAppearance(delay = 100) {
                FormSection(title = "General Information", icon = Icons.Outlined.Description) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Meal Name") },
                        placeholder = { Text("e.g. Grilled Salmon Salad") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 2
                    )

                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        label = { Text("Ingredients") },
                        placeholder = { Text("Salmon, Lettuce, Olive oil...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 3
                    )
                }
            }

            // Nutrition Section
            AnimatedAppearance(delay = 200) {
                FormSection(title = "Nutrition Facts", icon = Icons.Outlined.Restaurant) {
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it.filter { c -> c.isDigit() } },
                        label = { Text("Calories (kcal)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        suffix = { Text("kcal") }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = protein,
                            onValueChange = { protein = it },
                            label = { Text("Protein") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            suffix = { Text("g") }
                        )
                        OutlinedTextField(
                            value = carbs,
                            onValueChange = { carbs = it },
                            label = { Text("Carbs") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            suffix = { Text("g") }
                        )
                        OutlinedTextField(
                            value = fat,
                            onValueChange = { fat = it },
                            label = { Text("Fat") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            suffix = { Text("g") }
                        )
                    }
                }
            }

            // Schedule Section
            AnimatedAppearance(delay = 300) {
                FormSection(title = "Schedule", icon = Icons.Outlined.SetMeal) {
                    // Meal Type Dropdown
                    ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                        OutlinedTextField(
                            value = selectedType.name.lowercase().replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Meal Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                            viewModel.mealTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    onClick = { selectedType = type; typeExpanded = false }
                                )
                            }
                        }
                    }

                    // Day Dropdown
                    ExposedDropdownMenuBox(expanded = dayExpanded, onExpandedChange = { dayExpanded = it }) {
                        OutlinedTextField(
                            value = selectedDay,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Day of Week") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(expanded = dayExpanded, onDismissRequest = { dayExpanded = false }) {
                            viewModel.daysOfWeek.forEach { day ->
                                DropdownMenuItem(text = { Text(day) }, onClick = { selectedDay = day; dayExpanded = false })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun FormSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                }
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            content()
        }
    }
}
