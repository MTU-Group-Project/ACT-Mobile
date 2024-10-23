package mtu.gp.actmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import mtu.gp.actmobile.screen.AuthenticatedScreen
import mtu.gp.actmobile.screen.LoginScreen
import mtu.gp.actmobile.screen.RegisterScreen
import mtu.gp.actmobile.ui.theme.ACTMobileTheme

sealed class Screen(val route: String) {
    object Home: Screen("Home")
    object Contact: Screen("Contact")
    object Launch: Screen("Launch")
    object Prices: Screen("Prices")
    object Authenticated: Screen("Authenticated")
    object RegisterScreen: Screen("Register")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google Auth
        FirebaseAuth.getInstance()

        enableEdgeToEdge()

        setContent {
            Main(this)
        }
    }

     fun signIn(nav: NavHostController) {
         val activity = this

         lifecycleScope.launch {
             // Setting up sign in details
             val credentialManager = CredentialManager.create(activity)

             val googleIdOption = GetGoogleIdOption.Builder()
                 .setFilterByAuthorizedAccounts(false)
                 .setServerClientId(getString(R.string.client_id))
                 .setAutoSelectEnabled(false)
                 .setNonce("1123123") // TODO: make nonce
                 .build()

             val credentialRequest = GetCredentialRequest.Builder()
                 .addCredentialOption(googleIdOption)
                 .build()

             // Show menu
             val res: GetCredentialResponse?
             try {
                 res = credentialManager.getCredential(activity, credentialRequest)
             } catch (e: Exception) {
                 // May error out if User cancels, or if user never logged in
                 // in the first place
                 return@launch
             }

             val cred = res.credential

             FirebaseAuth.getInstance().signInWithCredential(
                 GoogleAuthProvider.getCredential(
                     GoogleIdTokenCredential.createFrom(cred.data).idToken,
                     null
                 )
             ).addOnSuccessListener {
                 nav.popBackStack()
                 nav.navigate(Screen.Authenticated.route)
             }.addOnFailureListener {
                // TODO: Display error message
             }
         }
     }
}

data class NavItem(
    var name: String,
    var image: ImageVector
)

@Composable
fun Main(activity: MainActivity) {
    val nav = rememberNavController()
    ACTMobileTheme {
        NavHost(nav, route = "root_graph", startDestination = "unauthenticated") {
            navigation(startDestination = Screen.Launch.route, route = "unauthenticated") {
                composable(Screen.Launch.route) { LaunchScreen(nav, activity) }
                composable(Screen.RegisterScreen.route) { RegisterScreen(nav, activity) }
            }

            composable(Screen.Authenticated.route) { AuthenticatedScreen() }
        }
    }
}

// Represents the menu the user sees before they log in (or register)
@Composable
fun LaunchScreen(nav: NavHostController, activity: MainActivity) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {},
        floatingActionButton = {}
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            LoginScreen(nav, activity)
        }
    }
}
