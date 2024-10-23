package mtu.gp.actmobile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ie.slin.assignment1.component.StockList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.type.Stock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

interface StockService {
    @GET("shares")
    suspend fun getStocks(): List<Stock>
}

@Composable
fun PurchaseAssetScreen(
    nav: NavHostController,
    stocks: MutableList<Stock>,
    favouriteStocks: MutableList<Stock>
) {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val stockService = retrofit.create(StockService::class.java) as StockService

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            stocks.clear()
            stocks.addAll(stockService.getStocks())
        }
    }

    Column(Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
        TitleText("Shares")
        Spacer(Modifier.height(10.dp))

        StockList(stocks, favouriteStocks, {}, { s ->
            if (!favouriteStocks.contains(s))
                favouriteStocks.add(s)
            else
                favouriteStocks.remove(s)
        })
    }
}
