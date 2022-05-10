package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object UserService {
    private val db = Firebase.firestore

    fun getUserData(callback: (result: MutableList<Korisnik>) -> Unit) {
        var lista: MutableList<Korisnik> = mutableListOf()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["isAdmin"].toString().toBoolean() == false)
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

    fun izmijeniSifru(id: String, novaSifra: String, callback: (result: Boolean) -> Unit) {
        var map = mutableMapOf<String, String>()
        map["password"] = novaSifra
        db.collection("users").whereEqualTo("id", id).get().addOnSuccessListener { QuerySnapshot ->
            if (QuerySnapshot.documents.isNotEmpty()) {
                for (person in QuerySnapshot)
                    db.collection("users").document(person.id).set(map, SetOptions.merge())
            }
            callback(true)
        }
    }

    fun izmijeniStatus(id: String, noviStatus: Boolean, callback: (result: Boolean) -> Unit) {
        var map = mutableMapOf<String, String>()
        map["isAdmin"] = noviStatus.toString()
        db.collection("users").whereEqualTo("id", id).get().addOnSuccessListener { QuerySnapshot ->
            if (QuerySnapshot.documents.isNotEmpty()) {
                for (person in QuerySnapshot)
                    db.collection("users").document(person.id).set(map, SetOptions.merge())
            }
            callback(true)
        }

    }

    fun dodajUBazu(
        email: String,
        password: String,
        isAdmin: Boolean,
        callback: (result: Boolean) -> Unit
    ) {
       // val documentReference = db.collection("users").document()
        /*
                    "id" to documentReference.id,
            "email" to email,
            "password" to password,
            "createdAt" to  Date(),
            "updatedAt" to Date(),
            "isLogged" to true
         */
        val user = hashMapOf(

          //  "id" to documentReference.id,
            "email" to email,
            "password" to password,
            "isAdmin" to isAdmin,
         //   "createdAt" to Date()
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

    fun dajKorisnika(id: String, callback: (result: Korisnik) -> Unit) {
        db.collection("users")
            .whereEqualTo("id", id)
            .get().addOnSuccessListener { querySnapshot ->
                val rezultat = querySnapshot.documents.first()
                callback(
                    Korisnik(
                        rezultat["id"].toString(),
                        rezultat["email"].toString(),
                        rezultat["password"].toString(),
                        rezultat["isAdmin"].toString().toBoolean(),
                        Date()
                    )
                )
            }

    }

    fun ObrisiKorisnika(id: String, callback: (result: Boolean) -> Unit) {
        db.collection("users").document(id).delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }

        }



    }
