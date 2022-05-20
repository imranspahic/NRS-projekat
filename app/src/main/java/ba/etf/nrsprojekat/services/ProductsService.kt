package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object ProductsService {
    private val db = Firebase.firestore;
    var products: MutableList<Product> = mutableListOf()
    var lista1 : MutableList<receivedProducts> = mutableListOf()
    var lista2 : MutableList<sentProducts> = mutableListOf()
    var lista3 : MutableList<deliveredProducts> = mutableListOf()

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
                         data["pdvCategoryName"]?.toString(),
                         data["quantity"].toString().toInt(),
                         data["price"]?.toString()?.toDouble() ?: 0.0,
                         data["status"].toString(),
                         data["mjernaJedinica"]?.toString(),
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
                   pdvCategoryName: String?,
                   quantity: Int,
                   price: Double,
                   status: String,
                   mjernaJedinica: String?,
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
                pdvCategoryName,
                quantity,
                price,
                status.lowercase(),
                mjernaJedinica,
                updatedDate
            )
            val newProductData = hashMapOf(
                "id" to newProduct.id,
                "name" to newProduct.name,
                "poslovnicaName" to newProduct.poslovnicaName,
                "pdvCategoryName" to newProduct.pdvCategoryName,
                "quantity" to newProduct.quantity,
                "price" to newProduct.price,
                "status" to newProduct.status,
                "mjernaJedinica" to newProduct.mjernaJedinica,
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
                "pdvCategoryName" to pdvCategoryName,
                "quantity" to quantity,
                "price" to price,
                "status" to status,
                "mjernaJedinica" to mjernaJedinica,
                "updatedAt" to updatedDate
            )
            db.collection("products").document(product.id).update(editedProductData).addOnSuccessListener {
               val index =  products.indexOfFirst { p -> p.id == product.id  }
                products[index].name = name
                products[index].poslovnicaName = poslovnicaName
                products[index].pdvCategoryName = pdvCategoryName
                products[index].quantity = quantity
                products[index].price = price
                products[index].status = status
                products[index].mjernaJedinica = mjernaJedinica
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
                        document.data["pdvCategoryName"].toString(),
                        document.data["quantity"].toString().toInt(),
                        document.data["price"]?.toString()?.toDouble() ?: 0.0,
                        document.data["status"].toString(),
                        document.data["mjernaJedinica"]?.toString(),
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
    fun addToSent(name: String,poslovnicaName: String,quantity: Int,status: String, callback: (result: Boolean) -> Unit) {
        val proizvod2 = hashMapOf(
            "name" to name ,
            "poslovnicaName" to poslovnicaName,
            "quantity" to quantity,
            "status" to status
        )

        db.collection("sentProducts")
            .add(proizvod2)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
    fun getSentProducts(callback: (result: MutableList<sentProducts>) -> Unit) {
        var lista: MutableList<sentProducts> = mutableListOf()
        db.collection("sentProducts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(
                        sentProducts(
                            document.data["name"].toString(),
                            document.data["poslovnicaName"].toString(),
                            document.data["quantity"].toString().toInt(),
                            document.data["status"].toString()
                        )
                    )
                    lista2.add(
                        sentProducts(
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
    fun addToDelivered(name: String,poslovnicaName: String,quantity: Int,status: String, callback: (result: Boolean) -> Unit) {
        val proizvod3 = hashMapOf(
            "name" to name ,
            "poslovnicaName" to poslovnicaName,
            "quantity" to quantity,
            "status" to status
        )

        db.collection("deliveredProducts")
            .add(proizvod3)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                callback(false)
            }
    }
    fun getDeliveredProducts(callback: (result: MutableList<deliveredProducts>) -> Unit) {
        var lista: MutableList<deliveredProducts> = mutableListOf()
        db.collection("deliveredProducts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(
                        deliveredProducts(
                            document.data["name"].toString(),
                            document.data["poslovnicaName"].toString(),
                            document.data["quantity"].toString().toInt(),
                            document.data["status"].toString()
                        )
                    )
                    lista3.add(
                        deliveredProducts(
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

 //funkcija dodjele
    fun FilterProducts(imePoslovnice : String, callback: (result: MutableList<Product>) -> Unit){

        var listafilter: MutableList<Product> = mutableListOf()
        db.collection("products").whereEqualTo("poslovnicaName", imePoslovnice).
        get().
        addOnSuccessListener { it ->
            for(document in it)
               // println(""+ document.data["poslovnicaName"].toString() + "  " + document.data["name"].toString())
                listafilter.add(Product(
                        document.data["id"].toString(),
                        document.data["name"].toString(),
                        document.data["poslovnicaName"].toString(),
                        document.data["pdvCategoryName"].toString(),
                        document.data["quantity"].toString().toInt(),
                    document.data["price"]?.toString()?.toDouble() ?: 0.0,
                        document.data["status"].toString(),
                        //document.data["mjernaJedinica"]?.toString(),
                    null,
                       (document.data["updatedAt"] as com.google.firebase.Timestamp).toDate()
                   // (document.data["createdAt"] as com.google.firebase.Timestamp).toDate()
                    )
            )
            //println(listafilter)
            callback(listafilter)
        }

    }

    fun updateProductQuantity(id: String, newQuantity: Int) {
        val editProductQuantity = mapOf(
            "quantity" to newQuantity
        )
        db.collection("products").document(id).update(editProductQuantity).addOnSuccessListener {}
    }

    fun updateProductStatus(id: String, newStatus: String) {
        val editProductStatus = mapOf(
            "status" to newStatus
        )
        db.collection("products").document(id).update(editProductStatus).addOnSuccessListener {}
    }

}