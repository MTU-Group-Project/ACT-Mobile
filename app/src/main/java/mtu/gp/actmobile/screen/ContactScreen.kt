package mtu.gp.actmobile.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val PHONE_NUMBER = "+353894560072"
const val APP_PACKAGE = "mtu.gp.actprototype"

@Composable
fun TitleText(text: String) {
    Text(text, fontSize = 30.sp, fontWeight = FontWeight.Black)
}

// Represents the contact screen, where a user
// can contact support through phone or email,
// and also rate the application
@Composable
fun ContactScreen() {
    val ctx = LocalContext.current

    var text by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
        // Phone to call support
        TitleText("Phone Support")
        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$PHONE_NUMBER")
            ctx.startActivity(intent)
        }) {
            Text("Call Support")
        }

        // Form to contact support (authenticated)
        Spacer(Modifier.height(30.dp))
        TitleText("Contact Support")

        TextField(
            text,
            { text = it },
            label = { Text("Message") })

        Button(
            {
                // TODO: Submit the message
            }
        ) {
            Text("Submit message")
        }

        // Leave rating
        Spacer(Modifier.height(30.dp))
        Button(
            {
                // TODO: fallback if fail?
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$APP_PACKAGE")
                ctx.startActivity(intent)
            }
        ) {
            Text("Leave a rating!")
        }
    }
}