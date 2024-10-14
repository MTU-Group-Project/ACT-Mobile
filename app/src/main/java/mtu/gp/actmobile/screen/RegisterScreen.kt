package mtu.gp.actmobile.screen

import android.util.Log
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
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen

@Composable
fun RegisterScreen(nav: NavHostController, activity: MainActivity) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Title and details
        Spacer(Modifier.height(30.dp))
        Text("ACT", fontWeight = FontWeight.Black, fontSize = 45.sp)
        Spacer(Modifier.height(40.dp))

        TextField(email, { email = it }, placeholder = { Text("Email") })

        Spacer(Modifier.height(10.dp))

        TextField(password, { password = it }, placeholder = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(10.dp))

        TextField(verifyPassword, { verifyPassword = it }, placeholder = { Text("Verify Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(10.dp))

        // Sign in button and handler
        Button(onClick = {
            if (password != verifyPassword) {
                // TODO: error handling
                return@Button
            }

            activity.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        nav.popBackStack()
                        nav.navigate(Screen.Launch.route)
                    } else {
                        email = "ERROR"
                        return@addOnCompleteListener
                    }
                }

        }) {
            Text("Register")
        }
    }
}
