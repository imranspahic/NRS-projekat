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
    var mapaZaNarudzbu = mutableMapOf<String, Any>()
    var lokacija: String? = null
    var mjesto: String? = null

    fun getOrders(id: String, callback: (result: MutableList<Narudzba>) -> Unit) {
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(!document.data["isDeleted"].toString().toBoolean() && document.data["idKupca"] == id)
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document.data["datumNarudzbe"] as com.google.firebase.Timestamp).toDate(),
                                document.data["lokacija"].toString(),
                                document.data["mjesto"].toString(),
                                (document.data["datumRacuna"] as? com.google.firebase.Timestamp)?.toDate(),
                                document.data["brojRacuna"]?.toString()?.toInt(),
                            )
                        )
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
            "listaProizvoda" to itemMapList,
            "lokacija" to lokacija,
            "mjesto" to mjesto,
            "datumRacuna" to null,
            "brojRacuna" to null
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

    fun getOrder(id: String?, callback: (narudzba: Narudzba, ukupnaCijena: Double) -> Unit,
                 failureCallback: () -> Unit
    )  {
        var finalPrice : Double = 0.0
        lateinit var narudzba: Narudzba
        db.collection("orders").document(id!!)
            .get()
            .addOnSuccessListener { document ->
                if(document == null) {
                    failureCallback()
                    return@addOnSuccessListener
                }
                    val mapa: MutableList<MutableMap<String, Any>> = document["listaProizvoda"] as MutableList<MutableMap<String, Any>>
                narudzba = Narudzba(
                                document["id"].toString(),
                                document["nazivNarudzbe"].toString(),
                                document["statusNarudzbe"].toString(),
                                document["idKupca"].toString(),
                                document["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document["datumNarudzbe"] as com.google.firebase.Timestamp).toDate(),
                                document["lokacija"].toString(),
                                document["mjesto"].toString(),
                                (document["datumRacuna"] as? com.google.firebase.Timestamp)?.toDate(),
                                document["brojRacuna"]?.toString()?.toInt(),
                                )

                        var brojac=0

                        while(brojac <= narudzba.proizvodi.size-1) {
                            finalPrice += narudzba.proizvodi[brojac].get("productPrice").toString().toDouble() * narudzba.proizvodi[brojac].get("quantity").toString().toDouble()
                            brojac++
                        }
                callback(narudzba, finalPrice)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
                failureCallback()
            }
    }

    fun setMapa() {
        for(item in ProductsService.products)
            if(item.quantity != 0)
                mapaZaNarudzbu.put(item.id, 0)
    }

    fun resetKolicinaProducts() {
        for(item in ProductsService.products)
            if(item.kolicinaNarudzbe != 0) item.kolicinaNarudzbe = 0
    }

    fun resetData() {
        imeTrenutneNarudzbe = null
        mapaZaNarudzbu = mutableMapOf()
        lokacija = null
        mjesto = null
    }
}