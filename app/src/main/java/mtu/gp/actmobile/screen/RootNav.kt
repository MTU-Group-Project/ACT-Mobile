package mtu.gp.actmobile.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.screen.launch.LoginScreen
import mtu.gp.actmobile.screen.launch.RegisterScreen

@Composable
fun RootNavigation(modifier: Modifier, activity: MainActivity) {
    val nav = rememberNavController()

    NavHost(nav, route = "root_graph", startDestination = "unauthenticated") {
        navigation(startDestination = Screen.Launch.route, route = "unauthenticated") {
            composable(Screen.Launch.route) { LoginScreen(nav, activity) }
            composable(Screen.RegisterScreen.route) { RegisterScreen(nav, activity) }
        }

        composable(Screen.Authenticated.route) { AuthenticatedScreen() }
    }
}