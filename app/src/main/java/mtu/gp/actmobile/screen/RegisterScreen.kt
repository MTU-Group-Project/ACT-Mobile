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
fun RegisterScreen(nav: NavHostController) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        Text("ACT", fontWeight = FontWeight.Black, fontSize = 45.sp)
        Spacer(Modifier.height(40.dp))

        TextField(username, { username = it }, placeholder = { Text("Username") })
        Spacer(Modifier.height(10.dp))
        TextField(password, { password = it }, placeholder = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(10.dp))
        TextField(verifyPassword, { verifyPassword = it }, placeholder = { Text("Verify Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            nav.popBackStack()
            nav.navigate(Screen.Launch.route)
        }) {
            Text("Register")
        }
    }
}
