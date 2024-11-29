package mtu.gp.actmobile.screen.authenticated

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.type.AIPrediction
import mtu.gp.actmobile.type.Stock

@Composable
fun ShareInformationScreen(nav: NavHostController, stock: Stock?) {
    // TODO: This has to be changed
    if (stock == null)
        return

    val scroll = rememberScrollState()

    val lows = mutableListOf<Point>()
    val highs = mutableListOf<Point>()

    var reportState by remember { mutableStateOf("AI report unrequested") }

    stock.history.forEachIndexed { i, h ->
        lows.add(Point(i.toFloat(), h.Low.toFloat()))
        highs.add(Point(i.toFloat(), h.High.toFloat()))
    }

    Column(Modifier.verticalScroll(scroll).fillMaxWidth()) {
        NiceButton("Back") {
            nav.popBackStack()
        }

        // TODO: change to appropriate width
        Row(Modifier.horizontalScroll(scroll).width(600.dp)) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = LineChartData(
                    linePlotData = LinePlotData(
                        lines = listOf(
                            Line(lows, LineStyle(color = Color.Red)),
                            Line(highs, LineStyle(color = Color.Green))
                        ),
                    ),
                    xAxisData = AxisData.Builder()
                        .axisStepSize(100.dp)
                        .backgroundColor(Color.White)
                        .axisLabelDescription { "Days Prior" }
                        .steps(30 - 1)
                        .labelData { i -> i.toString() }
                        .labelAndAxisLinePadding(15.dp)
                        .build(),
                    yAxisData = AxisData.Builder()
                        .axisLabelDescription { "Value" }
                        .backgroundColor(Color.White)
                        .labelAndAxisLinePadding(20.dp)
//                    .labelData { i ->
//                        val yScale = 100 / 5
//                        (i * yScale)
//                    }
                        .build(),
                    gridLines = GridLines(),
                    backgroundColor = Color.White
                )
            )
        }

        Text(stock.short_name)
        Text(stock.long_name)

        Text("${stock.price} ${stock.currency}")

        Button({
            GlobalScope.launch {
                while (true) {
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
                            reportState = data.report
                        }

                        if (data.state == "finished") {
                            break
                        }
                    } catch (e: Exception) {}

                    delay(1000)
                }
            }
        }) {
            Text("Use AI to generate stock information")
        }

        Text(reportState)
    }
}
