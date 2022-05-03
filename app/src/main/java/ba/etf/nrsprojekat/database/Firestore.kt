package ba.etf.nrsprojekat.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.system.*

object Firestore {
    private val db = Firebase.firestore

    fun createUser(email: String, password: String) {
        val documentReference = db.collection("users").document()
        val user = hashMapOf(
            "id" to documentReference.id,
            "email" to email,
            "password" to password,
            "createdAt" to  Date(),
            "isLogged" to true
        )
        documentReference.set(user)
    }

    fun checkIfEmailExists(email: String, callback: (result: Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .get().addOnSuccessListener { querySnapshot ->
                callback(querySnapshot.documents.size > 0)
            }
    }

    fun checkIfPasswordCorrect(email: String, password: String, callback: (result: Boolean) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get().addOnSuccessListener { querySnapshot ->
                callback(querySnapshot.documents.size > 0)
                if(querySnapshot.documents.isNotEmpty()) {
                    querySnapshot.documents.first().reference.update("isLogged", true)
                }
            }
    }
}