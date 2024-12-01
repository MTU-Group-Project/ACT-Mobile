package mtu.gp.actmobile.component;

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mtu.gp.actmobile.ui.theme.Blue
import mtu.gp.actmobile.ui.theme.LightBlue
import mtu.gp.actmobile.ui.theme.LighterBlue

@Composable
private fun NiceInput(value: String, placeholder: String,
                  hiddenText: Boolean, callback: (String) -> Unit,
                      number: Boolean
) {
    val vt = if (hiddenText) PasswordVisualTransformation() else VisualTransformation.None

    var keyboard = KeyboardOptions.Default

    if (number) {
        keyboard = keyboard.copy(keyboardType = KeyboardType.Number)
    }

    TextField(
        value,
        callback,
        label = { Text(placeholder) },
        visualTransformation = vt,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth().border(2.dp, Blue, RoundedCornerShape(8.dp)),
        keyboardOptions = keyboard,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = LighterBlue,
            focusedContainerColor = LighterBlue,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun NiceTextInput(value: String, placeholder: String = "",
                  hiddenText: Boolean = false, callback: (String) -> Unit
) {
    NiceInput(value, placeholder, hiddenText, callback, false)
}

@Composable
fun NiceIntInput(value: String, placeholder: String = "",
                  hiddenText: Boolean = false, callback: (String) -> Unit
) {
    NiceInput(value, placeholder, hiddenText, callback, true)
}

@Composable
fun NiceButton(label: String, callback: () -> Unit) {
    Button(
        callback,
        Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp), ambientColor = LightBlue),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Blue),

    ) {
        Text(label, Modifier.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
