package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.StringReader
import java.util.*

object BranchesService {
    private val db = Firebase.firestore;
    var branches: MutableList<Branch> = mutableListOf()

    fun getBranches(callback: (result: MutableList<Branch>) -> Unit) {
        var lista: MutableList<Branch> = mutableListOf()
        //branches = mutableListOf()
        db.collection("branches")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(
                        Branch(
                            document.data["id"].toString(),
                            document.data["nazivPoslovnice"].toString(),
                            document.data["mjesto"] as MutableList<String>,
                            (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                        )
                    )
                    /*branches.add(Branch(
                        document.data["id"].toString(),
                        document.data["nazivPoslovnice"].toString(),
                        document.data["mjesto"] as MutableList<String>,
                        (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                    ))*/
                }
                branches = lista
                callback(lista)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun addBranch(id: String, nazivPoslovnice: String, nazivMjesta: MutableList<String>, callback: (result: Boolean, mode: String) -> Unit) {
        val updatedDate = Date()
        if (id.isEmpty()) {
            Log.d("branches", "Dodavanje nove poslovnice")
            val documentReference = db.collection("branches").document()
          //  val itemList = mutableListOf<MutableList<String>>()
           // itemList.add(nazivMjesta)
            val place = hashMapOf(
                "id" to documentReference.id,
                "nazivPoslovnice" to nazivPoslovnice,
                "mjesto" to nazivMjesta,
                "createdAt" to Date(),
                "updatedAt" to updatedDate

            )
            documentReference.set(place).addOnSuccessListener {
                LoggingService.addLog(
                    LogAction.CREATE,
                    "Dodana poslovnica ${nazivPoslovnice}"
                ){}
                this.branches = branches.sortedWith(compareBy<Branch> { it.updatedAt }.reversed()).toMutableList()
                callback(true, "ADD")
            }.addOnFailureListener {
                callback(false, "ADD")
            }
        }
    }
}