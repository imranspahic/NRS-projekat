package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object LoginService {
    private val db = Firebase.firestore

    var logovaniKorisnik: Korisnik? = null

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
                    LoggingService.addLog(LogAction.LOGIN, "Korisnik ${email} se prijavio u aplikaciju"){}
                    logovaniKorisnik = Korisnik(
                        querySnapshot.documents.first()["id"].toString(),
                        email,
                        password,
                        querySnapshot.documents.first()["isAdmin"].toString().toBoolean(),
                        querySnapshot.documents.first()["poslovnica"].toString(),
                        (querySnapshot.documents.first()["updatedAt"] as com.google.firebase.Timestamp).toDate()
                    )
                }
            }
    }

    fun logoutUser() {
        db.collection("users").document(logovaniKorisnik!!.getID()).update("isLogged", false)
        LoggingService.addLog(LogAction.LOGOUT, "Korisnik ${logovaniKorisnik!!.getEmail()} se odjavio iz aplikacije"){}
    }

    fun changePassword(newPassword: String, callback: (result: Boolean) -> Unit ) {
        db.collection("users")
            .document(logovaniKorisnik!!.getID()).update("password", newPassword)
            .addOnSuccessListener {
                Log.d("changePassword", "success");
                LoggingService.addLog(LogAction.UPDATE, "Korisnik ${logovaniKorisnik!!.getEmail()} je promijenio šifru"){}
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
    fun checkIfAdmin(email: String , callback: (result: Boolean) -> Unit) {
        db.collection("users").whereEqualTo("email", email)
            .get().addOnSuccessListener {
                QuerySnapshot ->
                callback(QuerySnapshot.documents.first()["isAdmin"].toString().toBoolean())
            }
    }
}