package mtu.gp.actprototype.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mtu.gp.actprototype.Screen

@Composable
fun LoginScreen(nav: NavHostController) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        Text("ACT", fontWeight = FontWeight.Black, fontSize = 45.sp)
        Spacer(Modifier.height(40.dp))

        TextField(username, { username = it }, placeholder = { Text("Username") })
        Spacer(Modifier.height(10.dp))
        TextField(password, { password = it }, placeholder = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            nav.popBackStack()
            nav.navigate(Screen.Authenticated.route)
        }) {
            Text("Login")
        }

        Spacer(Modifier.height(15.dp))
        Text("OR", fontSize = 25.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(15.dp))

        Button({
            nav.navigate(Screen.RegisterScreen.route)
        }) {
            Text("Register new account")
        }

        Spacer(Modifier.height(15.dp))

        Button({
            // TODO: implement
            nav.popBackStack()
            nav.navigate(Screen.Authenticated.route)
        }) {
            Text("Sign in with Google")
        }
    }
}
