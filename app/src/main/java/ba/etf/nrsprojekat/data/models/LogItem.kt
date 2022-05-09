package ba.etf.nrsprojekat.data.models

import ba.etf.nrsprojekat.data.enums.LogAction
import java.util.*

data class LogItem(
    var id: String,
    var logAction: LogAction,
    var tekst: String,
    var createdAt: Date,
)