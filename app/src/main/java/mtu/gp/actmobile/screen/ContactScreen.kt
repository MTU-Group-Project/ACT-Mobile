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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import mtu.gp.actmobile.ui.theme.buttonColours


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
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

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
        }, colors = buttonColours) {
            Text("Call Support")
        }

        // Form to contact support (authenticated)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Contact Help", style = MaterialTheme.typography.titleLarge)

            // Name input field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            // Phone number input field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            // Feedback input field
            OutlinedTextField(
                value = feedback,
                onValueChange = { feedback = it },
                label = { Text("How can we help you?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) 
            )

            // Submit button
            Button(
                onClick = {
                    // Handle submission logic, e.g., validating inputs and processing
                },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColours
            ) {
                Text("Submit")
            }
        }


        // Leave rating
        Spacer(Modifier.height(30.dp))
        Button(
            {
                // TODO: fallback if fail?
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=$APP_PACKAGE")
                ctx.startActivity(intent)
            }, colors = buttonColours
        ) {
            Text("Leave a rating!")
        }
    }
}