package ie.slin.assignment1.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mtu.gp.actmobile.type.Stock
import mtu.gp.actmobile.ui.theme.Blue
import mtu.gp.actmobile.ui.theme.LighterBlue
import mtu.gp.actmobile.ui.theme.WarningRed
import mtu.gp.actmobile.ui.theme.White100

// This represents an entry in a list of stocks for a single stock
// onStockInteract represents when the user clicks on the button to favourite/unfavourite
@Composable
fun StockRow(
    stock: Stock,
    onStockSelect: (Stock) -> Unit
) {
    Card(Modifier.fillMaxWidth()
        .border(2.dp, Blue, RoundedCornerShape(8.dp))
        .clickable {
        onStockSelect(stock)
    },
        colors = CardDefaults.cardColors(containerColor = LighterBlue)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text("${stock.short_name} - ${stock.long_name}\n", style = MaterialTheme.typography.labelLarge)
            Text("${stock.price} ${stock.currency}")
        }
    }
}
