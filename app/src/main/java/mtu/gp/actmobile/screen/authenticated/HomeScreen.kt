package mtu.gp.actmobile.screen.authenticated

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.slin.assignment1.component.StockList
import mtu.gp.actmobile.StocksViewState
import mtu.gp.actmobile.type.Stock

@Composable
fun HomeScreen(nav: NavHostController, stocks: StocksViewState) {
    Column(Modifier.padding(20.dp)) {
        Text("Shares", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(10.dp))

        StockList(stocks.favouriteStocksState.collectAsState().value.stocks, nav)
    }

//    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
//        Button({
//            Firebase.auth.signOut()
//            nav.navigate(Screen.Launch.route)
//        }) {
//            Text("Log Out")
//        }
//
//        TitleText("Your Assets")
//        Spacer(Modifier.height(10.dp))
//
//        StockList(favouriteStocks, favouriteStocks, { s ->
//            selectedStock = s
//            nav.navigate(Screen.ShareInformationScreen.route)
//        }, { s ->
//            if (!favouriteStocks.contains(s))
//                favouriteStocks.add(s)
//            else
//                favouriteStocks.remove(s)
//
//            val stocks = favouriteStocks.map { it.short_name }
//
//            if (Firebase.auth.currentUser != null) {
//                Firebase.database.reference.child("fundadmin").child("${Firebase.auth.currentUser!!.uid}").child("favourites").setValue(stocks)
//            }
//        })
}

