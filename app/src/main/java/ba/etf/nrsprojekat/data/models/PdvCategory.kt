package ba.etf.nrsprojekat.data.models

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

data class PdvCategory(
    val id: String,
    var name: String,
    var pdvPercent: Double,
    val createdAt: Date,
    var updatedAt: Date
) {
    override fun toString(): String {
         val noDecimalFormat: NumberFormat = DecimalFormat.getInstance()
        noDecimalFormat.maximumFractionDigits = 0
        return name + " (${noDecimalFormat.format(pdvPercent)}%)"
    }
}