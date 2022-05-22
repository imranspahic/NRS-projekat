package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

object PdvCategoriesService {
    private val db = Firebase.firestore;
    var pdvCategories: MutableList<PdvCategory> = mutableListOf()

    fun fetchPdvCategories(callback: (result: Boolean) -> Unit) {
        db.collection("pdvCategories").get().addOnSuccessListener {
                querySnapshot ->
            pdvCategories = mutableListOf()
            querySnapshot.documents.forEach { document ->
                val data = document.data
                if(data != null) {
                    val newPdvCategory = PdvCategory(
                        data["id"].toString(),
                        data["name"].toString(),
                        data["pdvPercent"].toString().toDouble(),
                        (data["createdAt"]  as Timestamp).toDate(),
                        (data["updatedAt"]  as Timestamp).toDate()
                    )
                    pdvCategories.add(newPdvCategory)
                }
            }
            Log.d("pdvCategories", querySnapshot.documents.size.toString())
            pdvCategories = pdvCategories.sortedWith(compareBy<PdvCategory> { it.updatedAt }.reversed()) .toMutableList()
            callback(true)
        }.addOnFailureListener {
                callback(false)
            }
    }

    fun deleteCategory(id: String, callback: (result: Boolean) -> Unit) {
        db.collection("pdvCategories").document(id).delete().addOnSuccessListener {

            val category = pdvCategories.first { category -> category.id == id  }
            ProductsService.products.forEach { product ->
                if(product.pdvCategoryName == category.name) {
                    product.pdvCategoryName = null
                    ProductsService.updateProductPdvCategory(product.id, null)
                }
            }
            LoggingService.addLog(LogAction.DELETE, "Izbrisana PDV kategorija ${category.name}"){}
            pdvCategories.removeIf { category -> category.id == id }
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun addCategory(name: String, percent: String, callback: (result: Boolean) -> Unit) {
        Log.d("pdvCategories", "Dodavanje nove kategorije")
        val documentReference = db.collection("pdvCategories").document()
        var currentDate = Date()
        val newCategory = PdvCategory(
            documentReference.id,
            name,
            percent.toDouble(),
            currentDate,
            currentDate
        )
        val newCategoryData = hashMapOf(
            "id" to newCategory.id,
            "name" to newCategory.name,
            "pdvPercent" to newCategory.pdvPercent,
            "createdAt" to newCategory.createdAt,
            "updatedAt" to newCategory.updatedAt
        )
        documentReference.set(newCategoryData).addOnSuccessListener {
            pdvCategories.add(newCategory)
            LoggingService.addLog(LogAction.CREATE, "Dodana PDV kategorija ${newCategory.name}"){}
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun updateCategory(categoryID: String, name: String, percent: String, callback: (result: Boolean) -> Unit) {
        val pdvCategory: PdvCategory = pdvCategories.firstOrNull { category -> category.id == categoryID } ?: return
        var currentDate = Date()
        Log.d("pdvCategories", "Ažuriranje postojeće kategorije")
        val editedCategoryData = mapOf(
            "name" to name,
            "pdvPercent" to percent.toDouble(),
            "updatedAt" to currentDate
        )
       db.collection("pdvCategories").document(pdvCategory.id).update(editedCategoryData).addOnSuccessListener {
            val index =  pdvCategories.indexOfFirst { c -> c.id == pdvCategory.id  }

           ProductsService.products.forEach { product ->
               if(product.pdvCategoryName == pdvCategories[index].name) {
                   product.pdvCategoryName = name
                   ProductsService.updateProductPdvCategory(product.id, name)
               }
           }
           pdvCategories[index].name = name
           pdvCategories[index].pdvPercent = percent.toDouble()
           pdvCategories[index].updatedAt = currentDate
            LoggingService.addLog(LogAction.UPDATE, "Ažurirana PDV kategorija ${name}"){}
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }
}