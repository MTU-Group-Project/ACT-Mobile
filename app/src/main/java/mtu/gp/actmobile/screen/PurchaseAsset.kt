package mtu.gp.actprototype.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.gp.actprototype.Screen

enum class StockType {
    SHARE, CRYPTO
}

data class Financial(
    val type: StockType,
    val name: String,
    val value: Float
)


@Composable
fun StockInfo(nav: NavHostController, name: String, value: Float) {
    Row(Modifier.fillMaxWidth().background(Color.Black).padding(10.dp).clickable {
        nav.navigate(Screen.StockInfo.route)
    }) {
        Text("$name   ", color = Color.White)
        Text(value.toString(), color = Color.White)
        Text("   Predicted: XXX", color = Color.White)
        Text("   Add: ", color= Color.White)
        Checkbox(checked = false, onCheckedChange = {})
    }
}

@Composable
fun PurchaseAssetScreen(nav: NavHostController) {
    val shares = listOf(
        Financial(StockType.SHARE, "Apple (AAPL)", 165.2335f),
        Financial(StockType.SHARE, "Microsoft (MSFT)", 302.423f),
        Financial(StockType.SHARE, "Tesla (TSLA)", 234.7925f),
        Financial(StockType.SHARE, "Alphabet (GOOGL)", 125.6375f),
        Financial(StockType.SHARE, "Nvidia (NVDA)", 437.2185f),
        Financial(StockType.SHARE, "Meta (META)", 294.5475f),
        Financial(StockType.SHARE, "Amazon (AMZN)", 120.7735f),
        Financial(StockType.SHARE, "Intel (INTC)", 35.3495f),
        Financial(StockType.SHARE, "Netflix (NFLX)", 378.632f),
        Financial(StockType.SHARE, "AMD (AMD)", 97.3275f),
    )

    val crypto = listOf(
        Financial(StockType.CRYPTO, "XRP", 0.5035f),
        Financial(StockType.CRYPTO, "Solana (SOL)", 135.6885f),
        Financial(StockType.CRYPTO, "Binance Coin (BNB)", 527.57f)
    )

    Column(Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
        TitleText("Shares")
        Spacer(Modifier.height(10.dp))

        shares.forEach { s ->
            StockInfo(nav, s.name, s.value)
            Spacer(Modifier.height(10.dp))
        }

        TitleText("Crypto")
        Spacer(Modifier.height(10.dp))

        crypto.forEach { s ->
            StockInfo(nav, s.name, s.value)
            Spacer(Modifier.height(10.dp))

            Spacer(Modifier.height(10.dp))
        }
    }
}
