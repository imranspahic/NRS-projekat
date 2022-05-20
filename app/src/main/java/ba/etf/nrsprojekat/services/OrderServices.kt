package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


object OrderServices {
    private val db = Firebase.firestore
    var imeTrenutneNarudzbe: String? = null
   // var neispravnaKolicina: Boolean? = null
    fun getOrders(id: String, callback: (result: MutableList<Narudzba>) -> Unit) {
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["isDeleted"].toString().toBoolean() == false && document.data["idKupca"] == id)
                   // val mapa: MutableList<MutableMap<String, Any>> = document.data["listaProizvoda"] as MutableList<MutableMap<String, Any>>
                   // for(item in mapa) println("ITEM U MAPI ----------------->" + item)
                  //  var niz: ArrayMap<String, Int> = (ArrayMap<String, Int>()) (document.data("listaProizvoda"))
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as List<MutableMap<String, Any>>,
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

    fun addOrder(nazivNarudzbe: String, status: String, idKupca: String, mapa: MutableMap<String, Any>) {
        val documentReference = db.collection("orders").document()

        val itemMapList = mapa.map { m ->
            val proizvod: Product = ProductsService.products.first { p -> p.id == m.key }
            hashMapOf(
                "productID" to m.key,
                "quantity" to m.value,
                "productName" to proizvod.name,
                "productPrice" to proizvod.price,
                "productPdvCategory" to proizvod.pdvCategoryName,
                "productUnit" to proizvod.mjernaJedinica,
                "productPoslovnica" to proizvod.poslovnicaName
            )
        }
        val order = hashMapOf(
            "id" to documentReference.id,
            "datumNarudzbe" to Date(),
            "nazivNarudzbe" to nazivNarudzbe,
            "statusNarudzbe" to status,
            "isDeleted" to false,
            "idKupca" to idKupca,
            "listaProizvoda" to itemMapList
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
    fun getOrder(id: String?, callback: (result: MutableList<Narudzba>) -> Unit)  {
        var finalPrice : Double = 0.0
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                     val mapa: MutableList<MutableMap<String, Any>> = document.data["listaProizvoda"] as MutableList<MutableMap<String, Any>>
                    if(document["id"] == id) {
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document.data["datumNarudzbe"] as com.google.firebase.Timestamp).toDate()
                            )
                        )
                    }
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
            }
}
    fun getFinalPriceByOrder(id: String?, callback: (result: Double) -> Unit)  {
        var finalPrice : Double = 0.0
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                        val mapa: MutableList<MutableMap<String, Any>> = document.data["listaProizvoda"] as MutableList<MutableMap<String, Any>>

                    if(document["id"] == id) {
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document.data["datumNarudzbe"] as com.google.firebase.Timestamp).toDate()
                            )
                        )
                        var brojac=0

                        while(brojac <= lista[0].proizvodi.size-1) {
                            finalPrice += lista[0].proizvodi[brojac].get("productPrice").toString().toDouble() * lista[0].proizvodi[brojac].get("quantity").toString().toDouble()
                            brojac++
                        }
                    }
                }
                callback(finalPrice)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
            }
    }
    }