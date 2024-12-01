package mtu.gp.actmobile.screen.launch

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceTextInput

@Composable
fun RegisterScreen(nav: NavHostController, activity: MainActivity) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    BackgroundShapesAndLines()

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Column(Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text("ACT", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(32.dp))

            NiceTextInput(email, "Email") { email = it; email = email.trim() }
            Spacer(Modifier.height(10.dp))
            NiceTextInput(password, "Password", true) { password = it }
            Spacer(Modifier.height(10.dp))
            NiceTextInput(verifyPassword, "Verify Password", true) { verifyPassword = it }
            Spacer(Modifier.height(20.dp))

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

            Spacer(Modifier.height(20.dp))
            Text("Already have an account?",
                Modifier.clickable {
                    nav.navigate(Screen.Launch.route)
                },
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )

            OrContinueWith(activity, nav)
        }
    }
}
