package ba.etf.nrsprojekat.data.models

import java.util.*

class Narudzba(
    var id: String,
    var nazivNarudzbe: String,
    var status: String,
    var idKupca: String,
    var proizvodi: List<MutableMap<String, Any>>,
    var datumNarucivanja: Date
) {
    var isDeleted: Boolean = false

}
