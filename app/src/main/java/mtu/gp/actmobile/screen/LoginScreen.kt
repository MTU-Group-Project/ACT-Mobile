package mtu.gp.actmobile.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceTextInput

@Composable
fun LoginScreen(nav: NavHostController, activity: MainActivity) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Only display the login popup once
    var showOnce by remember { mutableIntStateOf(0) }
    if (showOnce == 0) {
        showOnce = 1

        activity.signIn(nav)
    }

    Column(Modifier.fillMaxSize().padding(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))
        Text("ACT", fontWeight = FontWeight.Black, fontSize = 72.sp)
        Spacer(Modifier.height(40.dp))

        // User Input
        NiceTextInput(email, "Email") { email = it; email = email.trim() }
        Spacer(Modifier.height(10.dp))
        NiceTextInput(password, "Password", true) { password = it }

        // Submit button
        NiceButton("Login") {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    nav.popBackStack()
                    nav.navigate(Screen.Authenticated.route)
                }.addOnFailureListener { e ->
                    Log.d("MACT", e.localizedMessage)
                }
        }

        Spacer(Modifier.height(15.dp))
        Text("OR", fontSize = 25.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(15.dp))

        NiceButton("Register new account") {
            nav.navigate(Screen.RegisterScreen.route)
        }

        Spacer(Modifier.height(15.dp))

        // Sign in with Google
        NiceButton("Sign in with Google") {
            activity.signIn(nav)
        }
    }
}
