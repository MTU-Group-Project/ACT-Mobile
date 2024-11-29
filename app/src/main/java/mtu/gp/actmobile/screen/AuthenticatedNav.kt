package mtu.gp.actmobile.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.StocksViewState
import mtu.gp.actmobile.screen.authenticated.ContactScreen
import mtu.gp.actmobile.screen.authenticated.HomeScreen
import mtu.gp.actmobile.screen.authenticated.PremiumBuyScreen
import mtu.gp.actmobile.screen.authenticated.PurchaseAssetScreen
import mtu.gp.actmobile.screen.authenticated.ShareInformationScreen
import mtu.gp.actmobile.type.Stock

data class NavItem(
    var name: String,
    var image: ImageVector,
    val label: String
)

// Represents what the user sees after they log in
@Composable
fun AuthenticatedScreen() {
    val nav = rememberNavController()
    var selectedNavItem by remember { mutableIntStateOf(0) }

    val bottomBars = listOf(
        NavItem(Screen.Home.route, Icons.Default.Home, "Home"),
        NavItem(Screen.Prices.route, Icons.Default.ShoppingCart, "Shares"),
        NavItem(Screen.Contact.route, Icons.Default.Email, "Contact"),
        NavItem(Screen.PremiumBuyScreen.route, Icons.Default.Star, "Premium")
    )

    val stocks = StocksViewState()

    LaunchedEffect(Unit) {
        stocks.updateStocks()
        stocks.updateFavourites()
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {},
        floatingActionButton = {},
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    bottomBars.forEachIndexed { i, e ->
                        NavigationBarItem(
                            selected = selectedNavItem == i,
                            onClick = {
                                selectedNavItem = i
                                nav.navigate(e.name)
                            },
                            icon = { Icon(e.image, e.name) },
                            label = { Text(e.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            route = Screen.Authenticated.route,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(nav, stocks) }
            composable(Screen.Contact.route) { ContactScreen() }
            composable(Screen.Prices.route) { PurchaseAssetScreen(nav, stocks) }
            composable("${Screen.ShareInformationScreen.route}/{share}") {
                val shareName = it.arguments?.getString("share")

                val stock = stocks.getStockByName(shareName ?: "")

                ShareInformationScreen(nav, stock)
            }

            composable(Screen.PremiumBuyScreen.route) { PremiumBuyScreen() }
        }
    }
}
