package mtu.gp.actmobile.type

import kotlinx.serialization.Serializable

@Serializable
data class AIPrediction(
    val state: String,
    val report: String?
)
