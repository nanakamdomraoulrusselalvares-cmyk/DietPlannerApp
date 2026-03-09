package com.guyanne.dietplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.guyanne.dietplanner.ui.navigation.DietPlannerNavGraph
import com.guyanne.dietplanner.ui.theme.DietPlannerTheme
import com.guyanne.dietplanner.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val darkMode by settingsViewModel.darkMode.collectAsState()

            DietPlannerTheme(darkTheme = darkMode) {
                DietPlannerNavGraph()
            }
        }
    }
}
