package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.data.models.receivedProducts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object ProductsService {
    private val db = Firebase.firestore;
    var products: MutableList<Product> = mutableListOf()
    var lista1 : MutableList<receivedProducts> = mutableListOf()

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
             this.products = products.sortedWith(compareBy<Product> { it.updatedAt }.reversed()) .toMutableList()
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
                   callback: (result: Boolean, mode: String) -> Unit) {

        val updatedDate = Date()

        //Adding product
        if(id.isEmpty()) {
            Log.d("products", "Dodavanje novog proizvoda")
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
                LoggingService.addLog(LogAction.CREATE, "Dodan proizvod ${newProduct.name}"){}
                callback(true, "ADD")
            }.addOnFailureListener {
                    callback(false, "ADD")
                }
        }

        //Editing product
        else {

            val product: Product = products.firstOrNull { product -> product.id == id } ?: return
            Log.d("products", "Ažuriranje postojećeg proizvoda")
            val editedProductData = mapOf(
                "name" to name,
                "poslovnicaName" to poslovnicaName,
                "quantity" to quantity,
                "status" to status,
                "updatedAt" to updatedDate
            )
            db.collection("products").document(product.id).update(editedProductData).addOnSuccessListener {
               val index =  products.indexOfFirst { p -> p.id == product.id  }
                products[index].name = name
                products[index].poslovnicaName = poslovnicaName
                products[index].quantity = quantity
                products[index].status = status
                products[index].updatedAt = updatedDate
                LoggingService.addLog(LogAction.UPDATE, "Ažuriran proizvod ${name}"){}
                callback(true, "EDIT")
            }.addOnFailureListener {
                callback(false, "EDOT")
            }
        }
    }

    fun deleteProduct(id: String, callback: (result: Boolean) -> Unit) {
        db.collection("products").document(id).delete().addOnSuccessListener {
            val product = products.first { product -> product.id == id  }
            LoggingService.addLog(LogAction.DELETE, "Izbrisan proizvod ${product.name}"){}
            products.removeIf { product -> product.id == id }
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }
    fun getDeliveryProducts(callback: (result: MutableList<Product>) -> Unit) {
        var lista: MutableList<Product> = mutableListOf()
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(Product(
                        document.data["id"].toString(),
                        document.data["name"].toString(),
                        document.data["poslovnicaName"].toString(),
                        document.data["quantity"].toString().toInt(),
                        document.data["status"].toString(),
                        (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                    ))
                }
                callback(lista)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
    fun addToReceived(name: String,poslovnicaName: String,quantity: Int,status: String, callback: (result: Boolean) -> Unit) {
        val proizvod1 = hashMapOf(
            "name" to name ,
            "poslovnicaName" to poslovnicaName,
            "quantity" to quantity,
            "status" to status
        )

        db.collection("receivedProducts")
            .add(proizvod1)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
    fun getReceivedProducts(callback: (result: MutableList<receivedProducts>) -> Unit) {
        var lista: MutableList<receivedProducts> = mutableListOf()
        db.collection("receivedProducts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(
                        receivedProducts(
                            document.data["name"].toString(),
                            document.data["poslovnicaName"].toString(),
                            document.data["quantity"].toString().toInt(),
                            document.data["status"].toString()
                        )
                    )
                    lista1.add(
                        receivedProducts(
                            document.data["name"].toString(),
                            document.data["poslovnicaName"].toString(),
                            document.data["quantity"].toString().toInt(),
                            document.data["status"].toString()
                        )
                    )
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
    fun getReceivedName(name : String ) : receivedProducts{
        val test = receivedProducts("","",0,"")
        val product: Product = products.firstOrNull { product -> product.name == name }?: return test
        val receivedProducts1 = receivedProducts(product.name,product.poslovnicaName,product.quantity,product.status)
        return receivedProducts1

    }


}