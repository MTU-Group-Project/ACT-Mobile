package mtu.gp.actmobile.screen.authenticated

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.navigation.NavHostController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import mtu.gp.actmobile.PurchaseInformation
import mtu.gp.actmobile.StocksViewState
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceIntInput
import mtu.gp.actmobile.component.NiceTextInput
import mtu.gp.actmobile.type.AIPrediction
import mtu.gp.actmobile.type.Stock
import mtu.gp.actmobile.ui.theme.Blue
import mtu.gp.actmobile.ui.theme.LightBlue
import kotlin.math.max
import kotlin.math.min

@Composable
fun ShareInformationScreen(nav: NavHostController, stock: Stock?,
                           stockState: StocksViewState, purchase: PurchaseInformation? = null
) {
    // TODO: This has to be changed
    if (stock == null)
        return

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val scroll = rememberScrollState()

    val lows = mutableListOf<Point>()
    val highs = mutableListOf<Point>()

    var stockAmount by remember { mutableIntStateOf(1) }
    var priceAlert by remember { mutableFloatStateOf(0f) }

    var reportState by remember { mutableStateOf("AI report unrequested") }

    stock.history.forEachIndexed { i, h ->
        lows.add(Point(i.toFloat(), h.Low.toFloat()))
        highs.add(Point(i.toFloat(), h.High.toFloat()))
    }

    val minY = min(lows.minBy { it.y }.y,  highs.minBy { it.y }.y)
    val maxY = max(lows.maxBy { it.y }.y,   highs.maxBy { it.y }.y)

    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(stock.short_name, style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.width(16.dp))

            Text(stock.long_name)
        }

        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(16.dp))
            Text("Price History", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            Row {
                Text("Current: ", fontWeight = FontWeight.Bold)
                Text("${stock.price} ${stock.currency}")
            }

    //        NiceButton("Back") {
    //            nav.popBackStack()
    //        }

            // TODO: change to appropriate width
            Row(Modifier.horizontalScroll(scroll).width(600.dp)) {
                LineChart(
                    modifier = Modifier.height(500.dp),
                    lineChartData = LineChartData(
                        linePlotData = LinePlotData(
                            lines = listOf(
                                Line(lows, LineStyle(color = Blue)),
                                Line(highs, LineStyle(color = Color(0xFF037B66)))
                            ),
                        ),
                        xAxisData = AxisData.Builder()
                            .axisLabelDescription { "Days Prior" }
                            .steps(30)
                            .labelData { it.toString() }
                            .build(),
                        yAxisData = AxisData.Builder()
                            .axisLabelDescription { "Value" }
                            .steps(30)
                            .labelData {
                                val step = (maxY - minY) / 30
                                ((it * step) + minY).toString()
                            }
                            .build()
                    )
                )
            }

            Row {
                Text("High ", color = Color(0xFF037B66), fontWeight = FontWeight.Bold)
                Text("Low", color = Blue, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))
            Text("Invest", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))


            // If this stock has not been purchased, then allow the user to do so
            if (purchase == null) {
                // TODO: Change to numerical input

                Row(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth(0.55f)) {
                        NiceIntInput(stockAmount.toString(), "Stock Amount") {
                            stockAmount = it.toIntOrNull() ?: 0
                        }
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        NiceButton("Buy Stock") {
                            if (stockAmount <= 0) {
                                Toast.makeText(
                                    context,
                                    "Invalid stock amount. Must be a number greater than 0",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@NiceButton
                            }

                            stockState.buyStock(stock, stockAmount)
                            Toast.makeText(context, "Made purchase!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            // If the stock has been purchased, allow the user to sell it
            else {
                NiceButton("Sell Stock") {
                    stockState.sellStock(purchase)
                    Toast.makeText(context, "Sold!", Toast.LENGTH_LONG).show()
                    nav.popBackStack()
                }

                Spacer(Modifier.height(16.dp))
                Text("Price Alerts", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))

                Row {
                    Column(Modifier.fillMaxWidth(0.4f)) {
                        NiceIntInput(priceAlert.toString(), "Price Alert Limit") {
                            priceAlert = it.toFloatOrNull() ?: 0f
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Column {
                        NiceButton("Set Price Alert") {
                            stockState.publishAlert(purchase, priceAlert)
                            Toast.makeText(context, "Set Price Alert!", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                LazyColumn(Modifier.height(300.dp)) {
                    items(stockState.getAlertsForPurchase(purchase)) {
                        Column {
                            Text(it.price.toString())

                            NiceButton("Remove") {
                                // TODO: This
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Text("AI Analytics", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))

            NiceButton("Use AI to generate stock information") {
                // TODO: Update when Wiktor fixes AI code
                scope.launch {
                    while (true) {
                        // TODO: Fix
                        val state = updateAI(stock) ?: break

                        reportState = state

                        delay(1000)
                    }
                }
            }

            Text(reportState)
        }
    }
}

suspend fun updateAI(stock: Stock): String? {
    try {
        val url =
            "https://get-report-xqeobirwha-uc.a.run.app?stock=${stock.short_name}"

        val json = Json {
            ignoreUnknownKeys = true
        }

        val client = HttpClient()

        val res = client.get(url)
        val data: AIPrediction = json.decodeFromString(res.body())

        if (data.report != null) {
            return data.report
        }

        if (data.state == "finished") {
            return null
        }
    } catch (_: Exception) {}

    return "Loading..."
}
