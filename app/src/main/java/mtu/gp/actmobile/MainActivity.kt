package mtu.gp.actmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import mtu.gp.actmobile.screen.AuthenticatedScreen
import mtu.gp.actmobile.screen.LoginScreen
import mtu.gp.actmobile.screen.RegisterScreen

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
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var signIn: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signIn = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build())

        FirebaseAuth.getInstance()

        enableEdgeToEdge()

        setContent {
            Main(this)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        Log.d("MACT", "Something happening")
//        try {
//            val acc = Identity.getSignInClient(this).getSignInCredentialFromIntent(data)
//
//            Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(acc.googleIdToken, null))
//                .addOnSuccessListener {
//                    Log.d("MACT", "Sign in complete")
//                }.addOnFailureListener {
//                    Log.d("MACT", "Sign in failed")
//                }
//        } catch(e: ApiException) {
//            Log.d("MACT", "An exception occurred")
//            e.printStackTrace()
//        }
//    }

     fun signIn(nav: NavHostController) {
         val activity = this

         lifecycleScope.launch {
             // Setting up signin details
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

             // TODO: Fix this
             FirebaseAuth.getInstance().signInWithCredential(
                 GoogleAuthProvider.getCredential(
                     GoogleIdTokenCredential.createFrom(cred.data).idToken,
                     null
                 )
             )
                 .addOnSuccessListener {
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

    NavHost(nav, route = "root_graph", startDestination = "unauthenticated") {
        navigation(startDestination = Screen.Launch.route, route = "unauthenticated") {
            composable(Screen.Launch.route) { LaunchScreen(nav, activity) }
            composable(Screen.RegisterScreen.route) { RegisterScreen(nav, activity) }
        }

        composable(Screen.Authenticated.route) { AuthenticatedScreen() }
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
