package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object BranchesService {
    private val db = Firebase.firestore;
    var branches: MutableList<Branch> = mutableListOf()

    fun getBranches(callback: (result: MutableList<Branch>) -> Unit) {
        var lista: MutableList<Branch> = mutableListOf()
        db.collection("branches")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(
                        Branch(
                            document.data["id"].toString(),
                            document.data["nazivPoslovnice"].toString(),
                            (document.data["updatedAt"]  as com.google.firebase.Timestamp).toDate()
                        )
                    )
                }
                callback(lista)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun addBranch(id: String, nazivPoslovnice: String,callback: (result: Boolean, mode: String) -> Unit) {
        val updatedDate = Date()
        if (id.isEmpty()) {
            Log.d("branches", "Dodavanje nove poslovnice")
            val documentReference = db.collection("branches").document()
            val newBranch = Branch(
                documentReference.id,
                nazivPoslovnice,
                updatedDate
            )
            val newBranchData = hashMapOf(
                "id" to newBranch.id,
                "nazivPoslovnice" to newBranch.nazivPoslovnice,
                "createdAt" to Date(),
                "updatedAt" to updatedDate
            )
            documentReference.set(newBranchData).addOnSuccessListener {
                branches.add(newBranch)
                LoggingService.addLog(
                    LogAction.CREATE,
                    "Dodana poslovnica ${newBranch.nazivPoslovnice}"
                ){}
                this.branches = branches.sortedWith(compareBy<Branch> { it.updatedAt }.reversed()).toMutableList()
                callback(true, "ADD")
            }.addOnFailureListener {
                callback(false, "ADD")
            }
        }
    }
}