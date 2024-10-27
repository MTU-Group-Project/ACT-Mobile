package mtu.gp.actmobile.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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

        Text(selectedStock!!.short_name)
        Text(selectedStock!!.long_name)

        Text("${selectedStock!!.price} ${selectedStock!!.currency}")
    }
}
