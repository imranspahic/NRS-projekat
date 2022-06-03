package ba.etf.nrsprojekat.data.models

import java.util.*

class Narudzba(
    var id: String,
    var nazivNarudzbe: String,
    var status: String,
    var idKupca: String,
    var proizvodi: List<MutableMap<String, Any>>,
    var datumNarucivanja: Date,
    var lokacija: String,
    var mjesto: String,
    var brojRacuna: Int?,
    var datumRacuna: String?,
    var vrijemeRacuna: String?
) {
    var imaFiskalni: Boolean = false


}
