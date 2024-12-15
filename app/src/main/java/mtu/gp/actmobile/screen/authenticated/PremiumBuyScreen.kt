package mtu.gp.actmobile.screen.authenticated

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.BarChart
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mtu.gp.actmobile.R
import mtu.gp.actmobile.component.NiceButton

// NOTE: Test card 4242 4242 4242 4242

@Serializable
data class PaymentSecret(
    val secret: String,
    val id: String
)

suspend fun getSecret(): PaymentSecret {
    val api = "https://act-premium-buy-xqeobirwha-uc.a.run.app"

    val json = Json {
        ignoreUnknownKeys = true
    }

    val res = HttpClient().post(api)
    val data: PaymentSecret = json.decodeFromString(res.body())

    return data
}

@Serializable
data class PurchaseStatus(
    val status: Int
)

suspend fun madePayment(paymentid: String): Boolean {
    val api = "https://act-premium-purchased-xqeobirwha-uc.a.run.app?payment_id=$paymentid"

    val json = Json {
        ignoreUnknownKeys = true
    }

    val res = HttpClient().get(api)

    val data: PurchaseStatus = json.decodeFromString(res.body())

    return data.status == 1
}

var premium = 0

@Composable
fun PremiumBuyScreen() {
    val context = LocalContext.current

    var secret: PaymentSecret? = null

    val scope = rememberCoroutineScope()

    val paymentSheet = rememberPaymentSheet { res ->
        onPaymentSheetResult(context, res, secret, scope)
    }

    LaunchedEffect(Unit) {
        Firebase.database.reference.child("fundadmin").child(Firebase.auth.currentUser!!.uid.toString())
            .child("premium").get().addOnSuccessListener { snap ->
                if (snap.exists())
                    premium = 1
            }
    }

    PaymentConfiguration.init(LocalContext.current, "pk_test_51QKcauDGbrVfwZ9w6XMHg8xzhWfaEot3WPf8ZLSbJbRdgpYzE17MRjLJ2oV5n6rgcUTkcf50FvfViEkwIqPULQiX00RzE4VAWm")

    Column {
        Text("ACT-AI Premium", style = MaterialTheme.typography.headlineMedium)

        Row(Modifier.fillMaxWidth()) {
            Image(painterResource(R.drawable.stocks_guy), "Stocks guy")
        }

        Text("To avail of premium AI insights, please pay for ACT-AI premium!")

        NiceButton("Pay for ACT-AI Premium") {
            scope.launch {
                secret = getSecret()
                paymentSheet.presentWithPaymentIntent(secret!!.secret)
            }
        }

        if (premium == 1) {
            Text("Congratulations! You own ACT-AI premium.")
        }
    }
}

private fun onPaymentSheetResult(context: Context, paymentSheetResult: PaymentSheetResult, secret: PaymentSecret?, scope: CoroutineScope) {
    when (paymentSheetResult) {
        is PaymentSheetResult.Completed -> {
            scope.launch {
                if (secret != null) {
                    madePayment(secret.id)
                }
            }

            Toast.makeText(context, "Payment Succeeded!", Toast.LENGTH_LONG).show()

            Firebase.database.reference.child("fundadmin").child(Firebase.auth.currentUser!!.uid.toString())
                .child("premium").setValue(1)
        }
        is PaymentSheetResult.Failed -> {
            Toast.makeText(context, "Payment Failed", Toast.LENGTH_LONG).show()
        }
        is PaymentSheetResult.Canceled -> {
            Toast.makeText(context, "Payment Cancelled", Toast.LENGTH_LONG).show()
        }
    }
}