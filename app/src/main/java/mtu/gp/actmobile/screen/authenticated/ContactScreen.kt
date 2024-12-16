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
import com.google.firebase.Firebase
import com.google.firebase.database.database
import mtu.gp.actmobile.component.NiceButton
import mtu.gp.actmobile.component.NiceIntInput
import mtu.gp.actmobile.component.NiceTextInput


const val PHONE_NUMBER = "+353894560072"

// Represents the contact screen, where a user
// can contact support through phone or email,
// and also rate the application
@Composable
fun ContactScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    var starsText by remember { mutableStateOf("") }
    var leaveCommentText by remember { mutableStateOf("") }

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
        NiceTextInput(name, "Name") {
            name = it
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
            val database = Firebase.database.reference
            val messageData = mapOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "feedback" to feedback
            )
            database.child("message").push().setValue(messageData)

            name = ""
            email = ""
            phone = ""
            feedback = ""
        }


        // Leave rating
        Spacer(Modifier.height(30.dp))

        NiceIntInput(starsText, "Stars") {
            starsText = it
        }

        Spacer(Modifier.height(8.dp))

        NiceTextInput(leaveCommentText, "Leave a Comment") {
            leaveCommentText = it
        }

        Spacer(Modifier.height(8.dp))
        NiceButton("Leave a rating!") {
            val database = Firebase.database.reference
            val ratingData = mapOf(
                "stars" to starsText.toIntOrNull(),
                "comment" to leaveCommentText
            )
            database.child("rating").push().setValue(ratingData)

            starsText = ""
            leaveCommentText = ""
        }
    }
}