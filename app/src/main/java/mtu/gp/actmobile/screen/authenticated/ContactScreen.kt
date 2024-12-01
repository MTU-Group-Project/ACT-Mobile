package mtu.gp.actmobile.screen.authenticated

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceIntInput
import mtu.gp.actmobile.component.NiceTextInput


const val PHONE_NUMBER = "+353894560072"
const val APP_PACKAGE = "mtu.gp.actprototype"

// Represents the contact screen, where a user
// can contact support through phone or email,
// and also rate the application
@Composable
fun ContactScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    val ctx = LocalContext.current
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Phone to call support
        Text("Phone Support", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        NiceButton("Call Support") {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$PHONE_NUMBER")
            ctx.startActivity(intent)
        }

        // Form to contact support
        Spacer(Modifier.height(20.dp))
        Text(text = "Contact Help", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        // Name input field
        NiceTextInput(email, "Name") {
            email = it
        }

        // Email input field
        Spacer(Modifier.height(10.dp))
        NiceTextInput(email, "Email") {
            email = it
        }

        // Phone number input field
        Spacer(Modifier.height(10.dp))
        NiceTextInput(phone, "Phone Number") {
            phone = it
        }

        // Feedback input field
        Spacer(Modifier.height(10.dp))
        NiceTextInput(feedback, "How can we help you?") {
            feedback = it
        }

        // Submit button
        Spacer(Modifier.height(20.dp))
        NiceButton("Submit") {
            // TODO: Submit
        }


        // Leave rating
        Spacer(Modifier.height(30.dp))

        NiceIntInput("Stars") {
            // TODO:
        }

        Spacer(Modifier.height(8.dp))

        NiceTextInput("Leave a Comment") {
            // TODO:
        }

        Spacer(Modifier.height(8.dp))
        NiceButton("Leave a rating!") {
            // TODO: fallback if fail?
            // TODO: Change the idea of this to the comment and stars system
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=$APP_PACKAGE")
            ctx.startActivity(intent)
        }
    }
}