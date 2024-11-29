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
data class Stock(
    val short_name: String,
    val long_name: String,
    val price: Float,
    val currency: String,
    val history: List<StockHistory>
)
