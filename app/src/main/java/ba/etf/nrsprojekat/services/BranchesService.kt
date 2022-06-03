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
    var mjesta: MutableList<String> = mutableListOf()

    fun getFiskalniInfo(id: String, callback: (result: Boolean) -> Unit) {

    }

    fun getBranches(callback: (result: MutableList<Branch>) -> Unit) {
        var lista: MutableList<Branch> = mutableListOf()
        //branches = mutableListOf()
        db.collection("branches")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    /*lista.add(
                        Branch(
                            document.data["id"].toString(),
                            document.data["nazivPoslovnice"].toString(),
                            document.data["mjesto"] as MutableList<String>,
                            (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                        )
                    )*/
                    val newBranch =  Branch(
                        document.data["id"].toString(),
                        document.data["nazivPoslovnice"].toString(),
                        document.data["mjesto"] as MutableList<String>,
                        (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                    )
                    lista.add(newBranch)
                    branches.add(newBranch)

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
            val place1 = Branch(documentReference.id,nazivPoslovnice,nazivMjesta,updatedDate)
            val place = hashMapOf(
                "id" to place1.id,
                "nazivPoslovnice" to place1.nazivPoslovnice,
                "mjesto" to place1.mjesto,
                "createdAt" to Date(),
                "updatedAt" to place1.updatedAt
            )
            documentReference.set(place).addOnSuccessListener {
                branches.add(place1)
                LoggingService.addLog(LogAction.CREATE, "Dodana poslovnica ${nazivPoslovnice}") {}
                this.branches = branches.sortedWith(compareBy<Branch> { it.updatedAt }.reversed())
                    .toMutableList()
                callback(true, "ADD")
            }.addOnFailureListener {
                callback(false, "ADD")
            }
        } else {
            val branch: Branch = branches.firstOrNull { branch -> branch.id == id } ?: return
            Log.d("branches", "Ažuriranje poslovnice")
            val editedPlace = mapOf(
                "nazivPoslovnice" to nazivPoslovnice,
                "mjesto" to nazivMjesta,
                "createdAt" to Date(),
                "updatedAt" to updatedDate
            )
            db.collection("branches").document(branch.id).update(editedPlace).addOnSuccessListener {
                val index = branches.indexOfFirst { p -> p.id == branch.id }
                branches[index].nazivPoslovnice = nazivPoslovnice
                branches[index].mjesto = nazivMjesta
                branches[index].updatedAt = updatedDate
                LoggingService.addLog(LogAction.UPDATE, "Ažurirana poslovnica ${nazivPoslovnice}") {}
                callback(true, "EDIT")
            }.addOnFailureListener {
                callback(false, "EDOT")
            }
        }
    }

    fun getID(imePoslovnice: String, callback: (result: String) -> Unit) {
        db.collection("branches").get().addOnSuccessListener {
            result ->
            for(branch in result)  if(branch.data["nazivPoslovnice"].toString() == imePoslovnice) {
                callback(branch.data["id"].toString())

            }
        }
    }

    fun getMjesta(imePoslovnice: String, callback: (result: Branch) -> Unit) {
        var newBranch: Branch? = null
        db.collection("branches")

         //   .whereEqualTo("nazivPoslovnice", imePoslovnice)
            .get()
            .addOnSuccessListener {
                result ->
                for(document in result) {
                    if(document.data["nazivPoslovnice"].toString() == imePoslovnice) {
                            newBranch =  Branch(
                            document.data["id"].toString(),
                            document.data["nazivPoslovnice"].toString(),
                            document.data["mjesto"] as MutableList<String>,
                            (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                        )
                     //   println("NEWBRANCH" + newBranch)
                        callback(newBranch!!)
                    }
                /*    if(newBranch != null) {
                        println("brenc" + newBranch)
                        callback(newBranch!!)
                    }  */
                }
              //  it -> println(it.documents.first()["nazivPoslovnice"].toString())
               // callback(it.documents.first()["mjesto"] as MutableList<String>)
            }
    }

}