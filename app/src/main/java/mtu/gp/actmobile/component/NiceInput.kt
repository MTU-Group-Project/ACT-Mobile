package mtu.gp.actmobile.component;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import mtu.gp.actmobile.ui.theme.Black100
import mtu.gp.actmobile.ui.theme.buttonColours

@Composable
fun NiceTextInput(value: String, placeholder: String = "", hiddenText: Boolean = false, callback: (String) -> Unit) {
    val vt = if (hiddenText) PasswordVisualTransformation() else VisualTransformation.None

    TextField(
        value,
        callback,
        label = { Text(placeholder) },
        visualTransformation = vt,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    )
}

@Composable
fun NiceButton(label: String, callback: () -> Unit) {
    Button(callback, colors = ButtonDefaults.buttonColors(containerColor = Black100)) {
        Text(label)
    }
}
