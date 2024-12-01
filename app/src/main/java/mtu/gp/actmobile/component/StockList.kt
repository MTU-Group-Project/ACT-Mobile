package ie.slin.assignment1.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.gp.actmobile.PurchaseInformation
import mtu.gp.actmobile.type.Stock

// This represents the list of stocks
@Composable
fun StockList(
    stocks: List<Stock>,
    nav: NavHostController
) {

    if (stocks.isEmpty()) {
        Text("Loading stocks...")
    }

    LazyColumn {
        items(stocks) { s ->
            StockRow(s) {
                nav.navigate("share_info/${s.short_name}")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// This represents the list of riders
@Composable
fun PurchasedStockList(
    purchases: List<PurchaseInformation>,
    nav: NavHostController
) {

    if (purchases.isEmpty()) {
        Text("No stocks added yet! Add a stock to see it here.")
    }

    LazyColumn {
        items(purchases) { p ->
            StockRow(p.stock, p) {
                nav.navigate("purchase_info/${p.uniqueId}")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
