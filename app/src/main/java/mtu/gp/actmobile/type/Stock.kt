package mtu.gp.actmobile.type

import kotlinx.serialization.Serializable

@Serializable
data class StockHistory(
    val Open: Double,
    val High: Double,
    val Low: Double,
    val Close: Double
)

@Serializable
data class EsgValues(
    val environment: Double? = null,
    val governance: Double? = null,
    val social: Double? = null,
    val total: Double? = null
)

@Serializable
data class Stock(
    val share_type: String,
    val short_name: String,
    val long_name: String,
    val price: Float,
    val currency: String,
    val history: List<StockHistory>,
    val esg: EsgValues
)
