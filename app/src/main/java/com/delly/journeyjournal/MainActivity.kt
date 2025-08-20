package com.delly.journeyjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.delly.journeyjournal.ui.HomeScreen
import com.delly.journeyjournal.ui.theme.JourneyJournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyJournalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavHost()
                }
            }
        }
    }
}

/**
 * Navigation graph for the app.
 */
@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("work") { WorkScreen(navController) }
        // Add other destinations
    }
}

/**
 * Temporary WorkScreen.
 */
@Composable
fun WorkScreen(navController: NavController) {
    // ... content for work screen
    Button(onClick = { navController.popBackStack() }) { // Example: Go back
        Text("Go Back")
    }
}