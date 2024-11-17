package mtu.gp.actmobile.type

data class StockHistory(
    val Open: Double,
    val High: Double,
    val Low: Double,
    val Close: Double
)

data class Stock(
    val short_name: String,
    val long_name: String,
    val price: Float,
    val currency: String,
    val history: List<StockHistory>
)