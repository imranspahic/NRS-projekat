package ba.etf.nrsprojekat.data.models

import java.util.*

data class Product(
    val id: String,
    var name: String,
    var poslovnicaName: String,
    var quantity: Int,
    var status: String,
    var updatedAt: Date
) {
    init {
        if(poslovnicaName.isEmpty()) {
            poslovnicaName = "Ime poslovnice"
        }
    }
}