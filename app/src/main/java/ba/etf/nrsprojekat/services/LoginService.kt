package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object LoginService {
    private val db = Firebase.firestore

    var logovaniKorisnik: Korisnik? = null

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
        logovaniKorisnik = Korisnik(
            documentReference.id,
            email,
            password,
            false
        )
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
                    logovaniKorisnik = Korisnik(
                        querySnapshot.documents.first()["id"].toString(),
                        email,
                        password,
                        false)
                }
            }
    }

    fun logoutUser() {
        db.collection("users").document(logovaniKorisnik!!.getID()).update("isLogged", false)
    }

    fun changePassword(newPassword: String, callback: (result: Boolean) -> Unit ) {
        db.collection("users")
            .document(logovaniKorisnik!!.getID()).update("password", newPassword)
            .addOnSuccessListener {
                Log.d("changePassword", "success");
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}