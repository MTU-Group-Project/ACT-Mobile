package mtu.gp.actmobile.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ie.slin.assignment1.component.StockList
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.type.Stock

@Composable
fun HomeScreen(nav: NavHostController, favouriteStocks: MutableList<Stock>) {
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Button({
            Firebase.auth.signOut()
            nav.navigate(Screen.Launch.route)
        }) {
            Text("Log Out")
        }
        TitleText("Your Assets")
        Spacer(Modifier.height(10.dp))

        StockList(favouriteStocks, favouriteStocks, {}, { s ->
            if (!favouriteStocks.contains(s))
                favouriteStocks.add(s)
            else
                favouriteStocks.remove(s)
        })
    }
}
