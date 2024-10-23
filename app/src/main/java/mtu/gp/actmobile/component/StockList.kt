package ie.slin.assignment1.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mtu.gp.actmobile.type.Stock

// This represents the list of riders
@Composable
fun StockList(
    stocks: List<Stock>,
    favourites: List<Stock>,
    onStockSelect: (Stock) -> Unit,
    onStockInteract: (Stock) -> Unit
) {

    if (stocks.isEmpty()) {
        Text("No stocks added yet! Add a stock to see it here.",
            modifier = Modifier.padding(16.dp))
    }

    stocks.forEach { s ->

        val isFavourite = favourites.contains(s)

        Box(Modifier.fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onStockSelect(s)
                },
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF000000)),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(width = 2.dp, color = Color(0xFF222222)),
            ) {
                StockRow(s, isFavourite, onStockInteract)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
