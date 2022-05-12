package ba.etf.nrsprojekat.data.models

data class sentProducts (
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