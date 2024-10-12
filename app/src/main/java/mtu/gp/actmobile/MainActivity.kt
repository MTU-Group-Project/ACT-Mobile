package mtu.gp.actprototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import mtu.gp.actprototype.screen.AuthenticatedScreen
import mtu.gp.actprototype.screen.LoginScreen
import mtu.gp.actprototype.screen.RegisterScreen

sealed class Screen(val route: String) {
    object Home: Screen("Home")
    object Contact: Screen("Contact")
    object Launch: Screen("Launch")
    object Prices: Screen("Prices")
    object Authenticated: Screen("Authenticated")
    object StockInfo: Screen("StockInfo")
    object RegisterScreen: Screen("Register")
}

class MainActivity : ComponentActivity() {
    var googleSignin: GoogleSignInClient? = null  // TODO: research

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // TODO: implement
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build() // TODO: more information required?

        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

data class NavItem(
    var name: String,
    var image: ImageVector
)

@Composable
fun Main() {
    val nav = rememberNavController()

    NavHost(nav, route = "root_graph", startDestination = "unauthenticated") {
        navigation(startDestination = Screen.Launch.route, route = "unauthenticated") {
            composable(Screen.Launch.route) { LaunchScreen(nav) }
            composable(Screen.RegisterScreen.route) { RegisterScreen(nav) }
        }

        composable(Screen.Authenticated.route) { AuthenticatedScreen() }
    }
}

// Represents the menu the user sees before they log in (or register)
@Composable
fun LaunchScreen(nav: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {},
        floatingActionButton = {}
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            LoginScreen(nav)
        }
    }
}
