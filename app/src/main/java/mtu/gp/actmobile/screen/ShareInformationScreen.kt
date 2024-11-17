package mtu.gp.actmobile.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.type.Stock

@Composable
fun ShareInformationScreen(nav: NavHostController) {
    // TODO: This has to be changed
    if (selectedStock == null)
        return



    Column(Modifier.verticalScroll(rememberScrollState())) {
        NiceButton("Back") {
            nav.popBackStack()
        }

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = listOf(Point(0f, 40f), Point(1f, 90f), Point(2f, 0f), Point(3f, 60f), Point(4f, 10f)),

                        )
                    ),
                ),
                xAxisData = AxisData.Builder()
                    .axisStepSize(100.dp)
                    .backgroundColor(Color.Blue)
                    .steps(5 - 1)
                    .labelData { i -> i.toString() }
                    .labelAndAxisLinePadding(15.dp)
                    .build(),
                yAxisData = AxisData.Builder()
                    .backgroundColor(Color.Red)
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

        Text(selectedStock!!.short_name)
        Text(selectedStock!!.long_name)

        Text("${selectedStock!!.price} ${selectedStock!!.currency}")

        Button({
            GlobalScope.launch {
                while (true) {

                    delay(1000)
                }
            }
        }) {
            Text("Use AI to generate stock information!")
        }
    }
}
