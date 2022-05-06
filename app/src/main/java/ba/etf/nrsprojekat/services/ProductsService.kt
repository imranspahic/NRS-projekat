package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ProductsService {
    private val db = Firebase.firestore;
    var products: MutableList<Product> = mutableListOf()

    fun fetchProducts(callback: (result: Boolean) -> Unit) {
         db.collection("products").get().addOnSuccessListener {
               querySnapshot ->
             products = mutableListOf()
             querySnapshot.documents.forEach { document ->
                 val data = document.data
                 if(data != null) {
                     val newProduct = Product(
                        data["id"].toString(),
                         data["name"].toString(),
                         data["poslovnicaID"].toString(),
                        data["poslovnicaName"].toString(),
                        data["quantity"].toString().toInt(),
                         data["status"].toString()
                     )
                     products.add(newProduct)
                 }
             }
             Log.d("products", querySnapshot.documents.size.toString())
             callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}