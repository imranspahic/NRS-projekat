package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserService {
    private val db = Firebase.firestore

     fun getUserData(callback: (result: MutableList<Korisnik>) -> Unit) {
        var lista: MutableList<Korisnik> = mutableListOf()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["isAdmin"].toString().toBoolean() == false)
                        lista.add(
                            Korisnik(
                            document.data["id"].toString(),
                            document.data["email"].toString(),
                            document.data["password"].toString(),
                            document.data["isAdmin"].toString().toBoolean(),
                            (document.data["updatedAt"] as Timestamp).toDate()
                        )
                        )
                    //  Log.d(TAG, "${document.id} => ${document.data}")
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

     fun izmijeniSifru(email: String, novaSifra: String, callback: (result: Boolean) -> Unit) {
        var map = mutableMapOf<String, String>()
        map["password"] = novaSifra
        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener {
                QuerySnapshot ->
            if(QuerySnapshot.documents.isNotEmpty()) {
                for(person in QuerySnapshot)
                    db.collection("users").document(person.id).set(map, SetOptions.merge())
            }
            callback(true)
        }
    }

     fun dodajUBazu(email: String, password: String, isAdmin: Boolean, callback: (result: Boolean) -> Unit) {
        //  println("U BAZA FUNK")
        val user = hashMapOf(
            "email" to email,
            "password" to password ,
            "isAdmin" to isAdmin
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                // println("DODAO JE")
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                //    println("NIJE DODAO")
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
}