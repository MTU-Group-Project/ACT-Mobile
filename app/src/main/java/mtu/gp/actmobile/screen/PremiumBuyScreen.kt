package mtu.gp.actmobile.screen

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.internal.http.HttpHeaders

@Serializable
data class PaymentSecret(
    val secret: String,
    val id: String
)

private var secret: String = ""
private var id: String = ""

suspend fun getSecret(): String {
    // Coordinates for Ballyhoura included
    val api = "http://10.0.2.2/act_premium"

    val json = Json {
        ignoreUnknownKeys = true
    }

    val client = HttpClient()

    val res = client.post(api)
    val data: PaymentSecret = json.decodeFromString(res.body())

    id = data.id

    return data.secret
}

42@Serializable
data class PurchaseStatus(
    val status: Int
)

suspend fun madePayment(paymentid: String): Boolean {
    val api = "http://10.0.2.2/act_premium_purchased"

    val json = Json {
        ignoreUnknownKeys = true
    }

    val client = HttpClient()

    val requestBody = "{\"payment_id\": \"$paymentid\"}"

    // Send the POST request with the body
    val res = client.post(api) {
        headers {
            append(io.ktor.http.HttpHeaders.ContentType, "application/json")
        }
        setBody(requestBody)
    }

    val data: PurchaseStatus = json.decodeFromString(res.body())

    return data.status == 1
}

@Composable
fun PremiumBuyScreen() {
    val context = LocalContext.current

    val paymentSheet = rememberPaymentSheet { res ->
        onPaymentSheetResult(context, res)
    }

    PaymentConfiguration.init(LocalContext.current, "pk_test_51QKcauDGbrVfwZ9w6XMHg8xzhWfaEot3WPf8ZLSbJbRdgpYzE17MRjLJ2oV5n6rgcUTkcf50FvfViEkwIqPULQiX00RzE4VAWm")

    Button({
        GlobalScope.launch {
            secret = getSecret()
            paymentSheet.presentWithPaymentIntent(secret)
        }
    }) {
        Text("Pay", color = Color.Black)
    }
}

private fun onPaymentSheetResult(context: Context, paymentSheetResult: PaymentSheetResult) {

    when (paymentSheetResult) {
        is PaymentSheetResult.Completed -> {
            GlobalScope.launch {
                madePayment(id)
            }

            Toast.makeText(context, "Payment Succeeded!", Toast.LENGTH_LONG).show()
        }
        is PaymentSheetResult.Failed -> {
            Toast.makeText(context, "Payment Failed", Toast.LENGTH_LONG).show()
        }
        is PaymentSheetResult.Canceled -> {
            Toast.makeText(context, "Payment Cancelled", Toast.LENGTH_LONG).show()
        }
    }
}