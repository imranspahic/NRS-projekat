package ba.etf.nrsprojekat.data.models

data class Product(
    val id: String,
    var name: String,
    var poslovnicaID: String,
    var poslovnicaName: String,
    var quantity: Int
)