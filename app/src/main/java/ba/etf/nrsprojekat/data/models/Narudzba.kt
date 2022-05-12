package ba.etf.nrsprojekat.data.models

import android.util.ArrayMap

class Narudzba(
    var id: String,
    var nazivNarudzbe: String,
    var status: String,
    var idKupca: String,
    var nizMapa: MutableMap<String, Any>
) {

}



/*
fun put(
    key: K,
    value: V
): V?
 */