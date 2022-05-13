package ba.etf.nrsprojekat.services

import android.util.Log
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.PdvCategory
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
            LoggingService.addLog(LogAction.DELETE, "Izbrisana PDV kategorija ${category.name}"){}
            pdvCategories.removeIf { category -> category.id == id }
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }
}