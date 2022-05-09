package ba.etf.nrsprojekat.data.models

import java.util.*

data class receivedProducts (
    var name: String,
    var poslovnicaName: String,
    var quantity: Int,
    var status: String
) {
    init {
        if(poslovnicaName.isEmpty()) {
            poslovnicaName = "Ime poslovnice"
        }
    }
}