package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object UserService {
    private val db = Firebase.firestore
    var users: MutableList<Korisnik> = mutableListOf()

    fun fetchUsers(callback: (result: Boolean) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                users = mutableListOf()
                Log.d("users", "Broj korisnika = ${result.documents.size}")
                for (document in result) {
                    Log.d("users", "document = ${document.data}")
                    if (!document.data["isAdmin"].toString().toBoolean())
                        users.add(
                            Korisnik(
                                document.data["id"].toString(),
                                document.data["email"].toString(),
                                document.data["password"].toString(),
                                document.data["isAdmin"].toString().toBoolean(),
                                document.data["poslovnica"].toString(),
                                (document.data["createdAt"] as Timestamp).toDate(),
                                (document.data["updatedAt"] as Timestamp).toDate()
                            )
                        )
                }
                this.users = users.sortedWith(compareBy<Korisnik> { it.createdAt }.reversed()).toMutableList()
                callback(true)
                Log.d("users", "Dodano u users = ${users.size}")
            }
            .addOnFailureListener { exception ->
                callback(false)
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun createUser(email: String, password: String, isAdmin: Boolean, poslovnica:String, callback: (result: Boolean) -> Unit) {
        val documentReference = db.collection("users").document()
        val createdAt = Date()
        val newUser = Korisnik(
            documentReference.id,
            email,
            password,
            isAdmin,
            poslovnica,
            createdAt,
            createdAt
        )
        val user = hashMapOf(
            "id" to newUser.getID(),
            "email" to newUser.getEmail(),
            "password" to newUser.getPassword(),
            "isAdmin" to newUser.isAdmin(),
            "poslovnica" to newUser.poslovnica(),
            "createdAt" to createdAt,
            "updatedAt" to createdAt,
            "isLogged" to true
        )
        documentReference.set(user).addOnSuccessListener {
            users.add(newUser)
            val logText = if(isAdmin) "Kreiran admin ${email}"
            else "Kreiran korisnik ${email}"
            LoggingService.addLog(LogAction.CREATE, logText){}
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun updateUser(id:String, newPassword: String, newAdminState: Boolean, newPoslovnica: String, callback: (result: Boolean) -> Unit) {
        val user: Korisnik = users.firstOrNull { user -> user.getID() == id } ?: return
        Log.d("users", "Ažuriranje postojećeg korisnika")
        val editedUserData = mapOf(
            "password" to newPassword,
            "isAdmin" to newAdminState,
            "updatedAt" to Date(),
            "poslovnica" to newPoslovnica
        )
        db.collection("users").document(id).update(editedUserData).addOnSuccessListener {
            val index =  users.indexOfFirst { u -> u.getID() == id }
            users[index].setPassword(newPassword);
            users[index].setAdmin(newAdminState)
            users[index].poslovnica = newPoslovnica

            val logText = if(newAdminState) "Ažuriran admin ${users[index].getEmail()}"
            else "Ažuriran korisnik ${users[index].getEmail()}"
            LoggingService.addLog(LogAction.UPDATE, logText){}
            callback(true,)
        }.addOnFailureListener {
            callback(false)
        }
    }


    fun deleteUser(id: String, callback: (result: Boolean) -> Unit) {
       db.collection("users").document(id).delete().addOnSuccessListener {
            val user = users.first { user -> user.getID() == id  }
            LoggingService.addLog(LogAction.DELETE, "Izbrisan korisnik ${user.getEmail()}"){}
            users.removeIf { user -> user.getID() == id }
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
        }

    fun updateUserPoslovnica(id: String, newPoslovnica: String, callback: () -> Unit) {
        val editedUserData = mapOf(
            "poslovnica" to newPoslovnica
        )
        db.collection("users").document(id).update(editedUserData).addOnSuccessListener {
            callback()
        }
    }
}
