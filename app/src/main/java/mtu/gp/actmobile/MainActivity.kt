package mtu.gp.actmobile

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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
import mtu.gp.actmobile.screen.RootNavigation
import mtu.gp.actmobile.type.Stock
import mtu.gp.actmobile.ui.theme.ACTMobileTheme
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Contact: Screen("contact")
    object Launch: Screen("launch")
    object Prices: Screen("shares")
    object Authenticated: Screen("authenticated")
    object RegisterScreen: Screen("register")
    object ShareInformationScreen: Screen("share_info")
    object PurchaseInformationScreen: Screen("purchase_info")
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

data class PurchaseInformation(
    val uniqueId: String,
    val stock: Stock,
    val amount: Int
)

data class PurchasesUiState(
    val purchases: List<PurchaseInformation> = emptyList()
)

data class PriceAlert(
    val uniqueId: String,
    val purchase: PurchaseInformation,
    val price: Float
)

data class PriceAlertsUiState(
    val alerts: List<PriceAlert> = emptyList()
)

class StocksViewState(val context: Context) : ViewModel() {
    private val _stocksState = MutableStateFlow(StocksUiState())
    val stocksState: StateFlow<StocksUiState> = _stocksState.asStateFlow()
    private val _purchasesState = MutableStateFlow(PurchasesUiState())
    val purchasesState: StateFlow<PurchasesUiState> = _purchasesState.asStateFlow()
    private val _alertsState = MutableStateFlow(PriceAlertsUiState())
    val alertsState: StateFlow<PriceAlertsUiState> = _alertsState.asStateFlow()

    init {
        val service = Executors.newSingleThreadScheduledExecutor()
        val handler = Handler(Looper.getMainLooper())
        service.scheduleWithFixedDelay({
            handler.run {
                updateStocks()
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    fun buyStock(stock: Stock, amount: Int) {
        val node = Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("purchases").push()

        node.child("name").setValue(stock.short_name)
        node.child("amount").setValue(amount)

        val p = _purchasesState.value.purchases.toMutableList()
        p.add(PurchaseInformation(node.key!!, stock, amount))
        _purchasesState.value = PurchasesUiState(p)
    }

    fun sellStock(purchase: PurchaseInformation) {
        val node = Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("purchases")
            .child(purchase.uniqueId)

        node.removeValue()

        val p = _purchasesState.value.purchases.toMutableList()
        p.remove(purchase)
        _purchasesState.value = PurchasesUiState(p)
    }

    fun getStockByName(name: String): Stock? {
        return _stocksState.value.stocks.firstOrNull { it.short_name == name }
    }

    fun getPurchaseById(id: String): PurchaseInformation? {
        return _purchasesState.value.purchases.firstOrNull { it.uniqueId == id }
    }

    fun updateStocks() {
        viewModelScope.launch {
            val json = Json {
                ignoreUnknownKeys = true
            }

            val res = HttpClient().get("https://get-stocks-xqeobirwha-uc.a.run.app")
            val data: List<Stock> = json.decodeFromString(res.body())

            val newState = StocksUiState(data)

            updateFavourites()

            checkAlerts(_stocksState.value, _alertsState.value)

            _stocksState.value = newState
        }
    }

    private fun checkAlerts(oldState: StocksUiState, newState: PriceAlertsUiState) {
        newState.alerts.forEach { a ->
            val newStock = a.purchase.stock
            val oldStock = oldState.stocks.firstOrNull { it.short_name == a.purchase.stock.short_name }
            val alert = a.price

            if (oldStock == null)
                return@forEach

            val oldPrice = oldStock.price
            val newPrice = newStock.price

//            if ((oldPrice < alert && newPrice >= alert) || (oldPrice > alert && newPrice <= alert)) {
//                showNotification(context)
//            }
        }
    }

    fun updateFavourites() {
        val purchases = mutableListOf<PurchaseInformation>()
        val alerts = mutableListOf<PriceAlert>()

        Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("purchases")
            .get()
            .addOnSuccessListener { data ->
                data.children.forEach { c ->
                    val amount = c.child("amount").value as Long
                    val stockName = c.child("name").value as String
                    val stock = _stocksState.value.stocks.firstOrNull { it.short_name == stockName }

                    if (stock != null) {
                        val purchase = PurchaseInformation(c.key!!, stock, amount.toInt())

                        purchases.add(purchase)

                        // Alerts
                        c.child("alerts").children.forEach { a ->
                            val price = a.child("price").getValue(Double::class.java) ?: 0f

                            alerts.add(PriceAlert(a.key!!, purchase, price.toFloat()))
                        }
                    }
                }

                _purchasesState.value = PurchasesUiState(purchases)
                _alertsState.value = PriceAlertsUiState(alerts)
            }
    }

    fun publishAlert(purchase: PurchaseInformation, priceAlert: Float) {
        val node = Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("purchases")
            .child(purchase.uniqueId)
            .child("alerts")
            .push()

        val a = _alertsState.value.alerts.toMutableList()
        a.add(PriceAlert(node.key!!, purchase, priceAlert))
        _alertsState.value = PriceAlertsUiState(a)

        node.child("price").setValue(priceAlert)
    }

    fun unpublishAlert(alert: PriceAlert) {
        Firebase.database.reference
            .child("fundadmin")
            .child(Firebase.auth.currentUser!!.uid)
            .child("purchases")
            .child(alert.purchase.uniqueId)
            .child("alerts")
            .child(alert.uniqueId)
            .removeValue()

        val a = _alertsState.value.alerts.toMutableList()
        a.remove(alert)
        _alertsState.value = PriceAlertsUiState(a)
    }
}
