package mtu.gp.actmobile.screen.launch

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mtu.gp.actmobile.MainActivity
import mtu.gp.actmobile.Screen
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceTextInput
import mtu.gp.actmobile.ui.theme.Blue
import mtu.gp.actmobile.ui.theme.LightGray
import mtu.gp.actmobile.R
import mtu.gp.actmobile.ui.theme.White100

@Composable
fun LoginScreen(nav: NavHostController, activity: MainActivity) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        activity.signIn(nav)
    }

    BackgroundShapesAndLines()

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Column(Modifier.padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text("ACT", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(32.dp))

            // Emal and password
            NiceTextInput(email, "Email") { email = it; email = email.trim() }
            Spacer(Modifier.height(10.dp))
            NiceTextInput(password, "Password", true) { password = it }
            Spacer(Modifier.height(20.dp))

            // Submit button
            NiceButton("Login") {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        nav.popBackStack()
                        nav.navigate(Screen.Authenticated.route)
                    }
            }

            Spacer(Modifier.height(20.dp))
            Text("Create new account",
                Modifier.clickable {
                    nav.navigate(Screen.RegisterScreen.route)
                },
                style = TextStyle(fontWeight = FontWeight.SemiBold)
            )

            OrContinueWith(activity, nav)
        }
    }
}

@Composable
fun BackgroundShapesAndLines() {
    val lineStroke = 3f

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        drawCircle(
            color = LightGray,
            radius = 100f,
            center = Offset(300f, 300f)
        )

        drawCircle(
            color = LightGray,
            radius = 150f,
            center = Offset(800f, 1000f)
        )

        drawLine(
            color = LightGray,
            start = Offset(0f, 100f),
            end = Offset(size.width, 100f),
            strokeWidth = lineStroke
        )

        drawLine(
            color = LightGray,
            start = Offset(0f, 300f),
            end = Offset(size.width, 300f),
            strokeWidth = lineStroke
        )

        drawLine(
            color = LightGray,
            start = Offset(0f, 500f),
            end = Offset(size.width, size.height),
            strokeWidth = lineStroke
        )

        drawLine(
            color = LightGray,
            start = Offset(0f, 800f),
            end = Offset(size.width, 0f),
            strokeWidth = lineStroke
        )

        drawLine(
            color = Color.LightGray.copy(alpha = 0.2f),
            start = Offset(0f, 300f),
            end = Offset(size.width, 300f),
            strokeWidth = lineStroke
        )
    }
}

@Composable
fun OrContinueWith(activity: MainActivity, nav: NavHostController) {
    Spacer(Modifier.height(40.dp))
    Text("Or continue with", style = TextStyle(color = Blue, fontWeight = FontWeight.SemiBold))
    Spacer(Modifier.height(20.dp))

    // Sign in with Google
    Box(Modifier.background(White100)
        .clip(RoundedCornerShape(8.dp))
        .border(2.dp, LightGray, RoundedCornerShape(16.dp))
        .padding(8.dp)
        .clickable {
            activity.signIn(nav)
        }.width(48.dp)
    ) {
        Image(painterResource(R.drawable.google), "Google Icon")
    }
}
