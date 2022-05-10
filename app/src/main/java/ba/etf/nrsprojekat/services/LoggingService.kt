package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.LogItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object LoggingService {
    private val db = Firebase.firestore;
    var logItems: MutableList<LogItem> = mutableListOf()

    fun addLog(logAction: LogAction, text: String, callback: (result: Boolean) -> Unit) {

        val documentReference = db.collection("logs").document()
        val newLog = LogItem(
            documentReference.id,
            logAction,
            text,
            Date()
        )
        val newLogData = hashMapOf(
            "id" to newLog.id,
            "logAction" to newLog.logAction.toString(),
            "text" to newLog.tekst,
            "createdAt" to newLog.createdAt
        )
        documentReference.set(newLogData).addOnSuccessListener {
            logItems.add(newLog)
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun fetchLogs(callback: (result: Boolean) -> Unit) {
        db.collection("logs").get().addOnSuccessListener {
                querySnapshot ->
            logItems = mutableListOf()
            querySnapshot.documents.forEach { document ->
                val data = document.data
                if(data != null) {
                    val newLog = LogItem(
                        data["id"].toString(),
                        LogAction.valueOf(data["logAction"].toString(),),
                        data["text"].toString(),
                        (data["createdAt"]  as com.google.firebase.Timestamp).toDate()
                    )
                    logItems.add(newLog)
                }
            }
            Log.d("logs", querySnapshot.documents.size.toString())
            this.logItems = logItems.sortedWith(compareBy<LogItem> { it.createdAt }.reversed()) .toMutableList()
            callback(true)
        }.addOnFailureListener {
                callback(false)
            }
    }

    fun getLoginLogs(): List<LogItem> {
        return logItems.filter { logItem -> logItem.logAction == LogAction.LOGIN  }
    }

    fun getLogoutLogs(): List<LogItem> {
        return logItems.filter { logItem -> logItem.logAction == LogAction.LOGOUT  }
    }

    fun getCreateLogs(): List<LogItem> {
        return logItems.filter { logItem -> logItem.logAction == LogAction.CREATE  }
    }

    fun getUpdateLogs(): List<LogItem> {
        return logItems.filter { logItem -> logItem.logAction == LogAction.UPDATE  }
    }

    fun getDeleteLogs(): List<LogItem> {
        return logItems.filter { logItem -> logItem.logAction == LogAction.DELETE  }
    }


}