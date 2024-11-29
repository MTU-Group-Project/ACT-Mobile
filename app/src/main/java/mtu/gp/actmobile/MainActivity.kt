package mtu.gp.actmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mtu.gp.actmobile.screen.AuthenticatedScreen
import mtu.gp.actmobile.screen.RootNavigation
import mtu.gp.actmobile.screen.launch.LoginScreen
import mtu.gp.actmobile.screen.launch.RegisterScreen
import mtu.gp.actmobile.type.Stock
import mtu.gp.actmobile.ui.theme.ACTMobileTheme

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Contact: Screen("contact")
    object Launch: Screen("launch")
    object Prices: Screen("shares")
    object Authenticated: Screen("authenticated")
    object RegisterScreen: Screen("register")
    object ShareInformationScreen: Screen("share_info")
    object PremiumBuyScreen: Screen("buy_premium")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ACTMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RootNavigation(Modifier.padding(innerPadding), this)
                }
            }
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

data class StocksUiState(
    val stocks: List<Stock> = emptyList()
)

class StocksViewState : ViewModel() {
    private val _stocksState = MutableStateFlow(StocksUiState())
    val stocksState: StateFlow<StocksUiState> = _stocksState.asStateFlow()
    private val _favouriteStocksState = MutableStateFlow(StocksUiState())
    val favouriteStocksState: StateFlow<StocksUiState> = _favouriteStocksState.asStateFlow()

//    fun addFavouriteStock(stock: Stock) {
//        if (favouriteStocks.contains(stock))
//            return
//
//        favouriteStocks.add(stock)
//    }

//    fun removeFavouriteStock(stock: Stock) {
//        favouriteStocks.value.remove(stock)
//    }

    fun getStockByName(name: String): Stock? {
        return _stocksState.value.stocks.firstOrNull { it.short_name == name }
    }

    suspend fun updateStocks() {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val res = HttpClient().get("https://get-stocks-xqeobirwha-uc.a.run.app")
        val data: List<Stock> = json.decodeFromString(res.body())

        _stocksState.value = StocksUiState(data)
    }

    fun updateFavourites() {
        if (Firebase.auth.currentUser == null) {
            return
        }

        Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("favourites")
            .get()
            .addOnSuccessListener { data ->
                val favourites = data.value as List<*>? ?: return@addOnSuccessListener

                val stocks = mutableListOf<Stock>()

                favourites.forEach { s ->
                    val stock = _stocksState.value.stocks.firstOrNull { it.short_name == s }

                    if (stock != null && !stocks.contains(stock))
                        stocks.add(stock)
                }

                _favouriteStocksState.value = StocksUiState(stocks)
            }
    }
}
