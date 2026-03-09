package com.guyanne.dietplanner.ui.screens

import android.graphics.Color as AndroidColor
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.guyanne.dietplanner.viewmodel.NutritionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionStatsScreen(onBack: () -> Unit, viewModel: NutritionViewModel = hiltViewModel()) {
    val summary by viewModel.summary.collectAsState()
    val weeklyCalories by viewModel.weeklyCalories.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Nutrition Stats", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Today's Focus Card
            AnimatedAppearance(delay = 0) {
                GoalProgressCard(summary.totalCalories, dailyGoal)
            }

            // Macro Breakdown
            AnimatedAppearance(delay = 100) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Macro Distribution",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("Protein", "${summary.totalProtein.toInt()}g", Color(0xFF42A5F5), Modifier.weight(1f))
                        StatCard("Carbs", "${summary.totalCarbs.toInt()}g", Color(0xFFFFCA28), Modifier.weight(1f))
                        StatCard("Fat", "${summary.totalFat.toInt()}g", Color(0xFFAB47BC), Modifier.weight(1f))
                    }
                }
            }

            // Charts section
            AnimatedAppearance(delay = 200) {
                StatsSection(title = "Nutrient Balance") {
                    Box(modifier = Modifier.height(280.dp).fillMaxWidth().padding(16.dp)) {
                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    isDrawHoleEnabled = true
                                    holeRadius = 50f
                                    setHoleColor(AndroidColor.TRANSPARENT)
                                    setTransparentCircleAlpha(0)
                                    legend.isEnabled = true
                                    legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
                                    setEntryLabelColor(AndroidColor.BLACK)
                                    setEntryLabelTextSize(10f)
                                    animateY(1000)
                                }
                            },
                            update = { chart ->
                                val entries = listOf(
                                    PieEntry(summary.totalProtein.coerceAtLeast(0.1f), "Protein"),
                                    PieEntry(summary.totalCarbs.coerceAtLeast(0.1f), "Carbs"),
                                    PieEntry(summary.totalFat.coerceAtLeast(0.1f), "Fat"),
                                )
                                val dataSet = PieDataSet(entries, "").apply {
                                    colors = listOf(
                                        AndroidColor.parseColor("#42A5F5"),
                                        AndroidColor.parseColor("#FFCA28"),
                                        AndroidColor.parseColor("#AB47BC")
                                    )
                                    sliceSpace = 4f
                                    valueTextSize = 12f
                                    valueTextColor = AndroidColor.WHITE
                                    valueTypeface = android.graphics.Typeface.DEFAULT_BOLD
                                }
                                chart.data = PieData(dataSet)
                                chart.invalidate()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            AnimatedAppearance(delay = 300) {
                StatsSection(title = "Weekly Performance") {
                    Box(modifier = Modifier.height(280.dp).fillMaxWidth().padding(16.dp)) {
                        AndroidView(
                            factory = { context ->
                                BarChart(context).apply {
                                    description.isEnabled = false
                                    legend.isEnabled = false
                                    setFitBars(true)
                                    xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                                    xAxis.setDrawGridLines(false)
                                    axisRight.isEnabled = false
                                    axisLeft.setDrawGridLines(true)
                                    axisLeft.gridColor = AndroidColor.LTGRAY
                                    axisLeft.gridLineWidth = 0.5f
                                    animateY(1000)
                                }
                            },
                            update = { chart ->
                                val labels = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
                                val entries = weeklyCalories.mapIndexed { i, (_, cal) ->
                                    BarEntry(i.toFloat(), cal.toFloat())
                                }
                                val dataSet = BarDataSet(entries, "Calories").apply {
                                    color = AndroidColor.parseColor("#66BB6A")
                                    valueTextSize = 10f
                                    setDrawValues(false)
                                }
                                chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                                chart.data = BarData(dataSet).apply { barWidth = 0.6f }
                                chart.invalidate()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GoalProgressCard(current: Int, goal: Int) {
    val progress = if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1.2f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Daily Calories",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        "$current / $goal kcal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(CircleShape),
                color = if (progress > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = if (progress >= 1f) "Goal reached! 🏆" else "${((1f - progress) * goal).toInt()} kcal remaining to reach your goal",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun StatsSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            content()
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}
