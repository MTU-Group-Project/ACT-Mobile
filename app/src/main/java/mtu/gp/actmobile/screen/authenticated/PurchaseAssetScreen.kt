package mtu.gp.actmobile.screen.authenticated

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.slin.assignment1.component.StockList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mtu.gp.actmobile.StocksViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseAssetScreen(
    nav: NavHostController,
    stocks: StocksViewState,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column {
        Text("Shares", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(10.dp))

        PullToRefreshBox(isRefreshing, {
            scope.launch {
                isRefreshing = true
                stocks.updateStocks()
                delay(1000)
                isRefreshing = false
            }
        }) {
            StockList(stocks.stocksState.collectAsState().value.stocks, nav)
        }
    }
}
