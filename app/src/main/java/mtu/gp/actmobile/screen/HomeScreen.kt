package mtu.gp.actmobile.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(nav: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        TitleText("Your Assets")
        Spacer(Modifier.height(10.dp))

        StockInfo(nav, "Apple", 500.0f)
    }
}
