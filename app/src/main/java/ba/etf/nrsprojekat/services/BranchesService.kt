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
    //var branches: MutableList<Branch> = mutableListOf()

    fun getBranches(callback: (result: MutableList<Branch>) -> Unit) {
        var lista: MutableList<Branch> = mutableListOf()
        BranchesService.db.collection("branches")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(Branch(
                        document.data["id"].toString(),
                        document.data["nazivPoslovnice"].toString(),

                        ))
                }
                callback(lista)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}