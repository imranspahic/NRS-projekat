package ba.etf.nrsprojekat.data.models

import java.util.*

data class Branch(
    val id: String,
    var nazivPoslovnice : String,
    var mjesto: MutableList<String>,
    var updatedAt: Date
)