package ba.etf.nrsprojekat.data.models

import android.util.ArrayMap
import java.util.*

class Narudzba(
    var id: String,
    var nazivNarudzbe: String,
    var status: String,
    var idKupca: String,
    var nizMapa: MutableMap<String, Any>,
    var datumNarucivanja: Date
) {
    var isDeleted: Boolean = false

}



/*
fun put(
    key: K,
    value: V
): V?
 */