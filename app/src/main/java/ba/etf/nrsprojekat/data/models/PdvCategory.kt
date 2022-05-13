package ba.etf.nrsprojekat.data.models

import java.util.*

data class PdvCategory(
    val id: String,
    var name: String,
    var pdvPercent: Double,
    val createdAt: Date,
    var updatedAt: Date
)