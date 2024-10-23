package ie.slin.assignment1.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mtu.gp.actmobile.type.Stock
import mtu.gp.actmobile.ui.theme.WarningRed
import mtu.gp.actmobile.ui.theme.White100

// This represents an entry in a list of stocks for a single stock
// onStockInteract represents when the user clicks on the button to favourite/unfavourite
@Composable
fun StockRow(
    stock: Stock,
    isFavourite: Boolean,
    onStockInteract: (Stock) -> Unit
) {
    Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Start) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Column {
                Text("${stock.short_name} - ${stock.long_name}\n${stock.price} ${stock.currency}",
                    fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = White100)

                if (isFavourite) {
                    OutlinedButton(onClick = {
                        onStockInteract(stock)
                    }) {
                        Icon(Icons.Default.Delete, "Delete", tint = WarningRed)
                    }
                } else {
                    OutlinedButton(onClick = {
                        onStockInteract(stock)
                    }) {
                        Icon(Icons.Default.Add, "Add", tint = White100)
                    }
                }
            }
        }
    }
}
