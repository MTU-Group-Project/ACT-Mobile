package mtu.gp.actmobile.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.signin.internal.SignInClientImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen

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

//    val signIn = rememberLauncherForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        try {
//            val acc = Identity.getSignInClient(activity).getSignInCredentialFromIntent(result.data)
//
//            Firebase.auth.signInWithCredential(GoogleAuthProvider.getCredential(acc.googleIdToken, null))
//                .addOnSuccessListener {
//                    nav.navigate(Screen.Authenticated.route)
//                }.addOnFailureListener {
//                    // TODO: Display error
//                }
//        } catch(e: ApiException) {
//            e.printStackTrace()
//        }
//    }

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        Text("ACT", fontWeight = FontWeight.Black, fontSize = 45.sp)
        Spacer(Modifier.height(40.dp))

        TextField(email, { email = it }, placeholder = { Text("Email") })
        Spacer(Modifier.height(10.dp))
        TextField(password, { password = it }, placeholder = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Button(onClick = {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    nav.popBackStack()
                    nav.navigate(Screen.Authenticated.route)
                }.addOnFailureListener {
                    // TODO: Error message
                }
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

        // Sign in with Google
        Button({
            if (activity.signIn != null) {
//                activity.startActivityForResult(activity.signIn!!.signInIntent, 5)
                activity.signIn(nav)
//                activity.lifecycleScope.launch {
//                    activity.signIn()
//                }
            }
        }) {
            Text("Sign in with Google")
        }
    }
}
