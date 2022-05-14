package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.ArrayMap
import android.util.Log
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.data.models.Narudzba
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object OrderServices {
    private val db = Firebase.firestore
    var imeTrenutneNarudzbe: String? = null
   // var neispravnaKolicina: Boolean? = null
    fun getOrders(callback: (result: MutableList<Narudzba>) -> Unit) {
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["isDeleted"].toString().toBoolean() == false)
                   // val mapa: MutableList<MutableMap<String, Any>> = document.data["listaProizvoda"] as MutableList<MutableMap<String, Any>>
                   // for(item in mapa) println("ITEM U MAPI ----------------->" + item)
                  //  var niz: ArrayMap<String, Int> = (ArrayMap<String, Int>()) (document.data("listaProizvoda"))
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as MutableMap<String, Any>,
                                (document.data["datumNarudzbe"] as com.google.firebase.Timestamp).toDate()
                        )
                            )
                    //  Log.d(TAG, "${document.id} => ${document.data}")
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
            }
    }

    fun addOrder(nazivNarudzbe: String, status: String, /*idKupca: String,*/ mapa: MutableMap<String, Any>) {
        val documentReference = OrderServices.db.collection("orders").document()
        /*
        val user = hashMapOf(
            "id" to documentReference.id,
            "email" to email,
            "password" to password,
            "isAdmin" to isAdmin,
            "createdAt" to  Date(),
            "updatedAt" to Date(),
            "isLogged" to true
        )
         */
        val order = hashMapOf(
            "id" to documentReference.id,
            "datumNarudzbe" to Date(),
            "nazivNarudzbe" to nazivNarudzbe,
            "statusNarudzbe" to status,
            "isDeleted" to false,
            "listaProizvoda" to mapa
        )
        documentReference.set(order)
    }
    fun updateOrder(id: String) { // staviti id
        val updateOrder = mapOf(
            "isDeleted" to true
        )
        db.collection("orders").document(id).update(updateOrder).addOnSuccessListener {

        }

    }

/*
    var id: String,
    var nazivNarudzbe: String,
    var status: String,
    var idKupca: String,
    var nizMapa: ArrayMap<String, Int>
 */


}