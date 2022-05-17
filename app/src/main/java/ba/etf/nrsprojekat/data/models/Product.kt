package ba.etf.nrsprojekat.data.models

import java.util.*

data class Product(
    val id: String,
    var name: String,
    var poslovnicaName: String,
    var pdvCategoryName: String?,
    var quantity: Int,
    var price: Double,
    var status: String,
    var mjernaJedinica: String?,
    var updatedAt: Date,
) {
    var kolicinaNarudzbe: Int = 0
    init {
        if(poslovnicaName.isEmpty()) {
            poslovnicaName = "Ime poslovnice"
        }
    }
}