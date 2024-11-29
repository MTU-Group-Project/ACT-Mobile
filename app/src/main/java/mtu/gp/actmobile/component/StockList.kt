package ie.slin.assignment1.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.gp.actmobile.type.Stock

// This represents the list of riders
@Composable
fun StockList(
    stocks: List<Stock>,
    nav: NavHostController
) {

    if (stocks.isEmpty()) {
        Text("No stocks added yet! Add a stock to see it here.")
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
