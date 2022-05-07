package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

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
                     val newProduct = Product(data["id"].toString(),
                         data["name"].toString(),
                         data["poslovnicaName"].toString(),
                         data["quantity"].toString().toInt(),
                         data["status"].toString(),
                         (data["updatedAt"]  as com.google.firebase.Timestamp).toDate()
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

    fun addProduct(id: String,
                   name: String,
                   poslovnicaName: String,
                   quantity: Int,
                   status: String,
                   callback: (result: Boolean) -> Unit) {

        val updatedDate = Date()

        //Adding product
        if(id.isEmpty()) {
            val documentReference = db.collection("products").document()
            val newProduct = Product(
                documentReference.id,
                name,
                poslovnicaName,
                quantity,
                status.lowercase(),
                updatedDate
            )
            val newProductData = hashMapOf(
                "id" to newProduct.id,
                "name" to newProduct.name,
                "poslovnicaName" to newProduct.poslovnicaName,
                "quantity" to newProduct.quantity,
                "status" to newProduct.status,
                "createdAt" to Date(),
                "updatedAt" to updatedDate
            )
            documentReference.set(newProductData).addOnSuccessListener {
                products.add(newProduct)
                callback(true)
            }.addOnFailureListener {
                    callback(false)
                }
        }

        //Editing product
        else {
          val product: Product = products.firstOrNull { product -> product.id == id } ?: return
            val editedProductData = hashMapOf(
                "name" to name,
                "poslovnicaName" to poslovnicaName,
                "quantity" to quantity,
                "status" to status,
                "updatedAt" to updatedDate
            )
            db.collection("products").document(product.id).set(editedProductData).addOnSuccessListener {
               val index =  products.indexOfFirst { p -> p.id == product.id  }
                products[index].name = name
                products[index].poslovnicaName = poslovnicaName
                products[index].quantity = quantity
                products[index].status = status
                products[index].updatedAt = updatedDate
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
        }
    }
}