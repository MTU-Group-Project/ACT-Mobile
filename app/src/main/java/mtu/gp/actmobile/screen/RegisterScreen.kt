package mtu.gp.actmobile.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.component.NiceButton

@Composable
fun RegisterScreen(nav: NavHostController, activity: MainActivity) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {},
        floatingActionButton = {}
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {
            // Title and details

            NiceButton("Back") {
                nav.navigate(Screen.Launch.route)
            }

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
            NiceButton("Register") {
                if (password != verifyPassword) {
                    Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_LONG).show()
                    return@NiceButton
                }

                if (email.isEmpty()) {
                    Toast.makeText(context, "Email is empty!", Toast.LENGTH_LONG).show()
                    return@NiceButton
                }

                if (password.isEmpty()) {
                    Toast.makeText(context, "Password is empty!", Toast.LENGTH_LONG).show()
                    return@NiceButton
                }

                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        nav.popBackStack()
                        nav.navigate(Screen.Launch.route)
                    }
                    .addOnFailureListener { fail ->
                        Toast.makeText(context, fail.message, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}
