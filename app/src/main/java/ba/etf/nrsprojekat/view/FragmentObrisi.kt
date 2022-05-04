package ba.etf.nrsprojekat.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.firestore.*

class FragmentObrisi : Fragment(R.layout.obrisi_fragment) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Korisnik>
    private lateinit var myAdapter: Adapter
    private lateinit var db: FirebaseFirestore
    private lateinit var btnDelete: Button
    private lateinit var email: TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
      //  recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()


        getUserData {
            result -> myAdapter=Adapter(result)
            println(result[0].email)
            recyclerView.adapter = myAdapter
        }

       // myAdapter = Adapter(userArrayList)
        //recyclerView.adapter = myAdapter
        //getAllUsersFromDatabase()

        btnDelete = view.findViewById(R.id.deleteUser)
        email = view.findViewById(R.id.inputEmail)

        btnDelete.setOnClickListener {
            if (email.text.isNotEmpty()) {
                val query = db.collection("users")
                    .whereEqualTo("email", email.text.toString())
                    .get()
                query.addOnSuccessListener {
                    for (document in it) {
                        db.collection("users").document(document.id).delete()
                        Toast.makeText(view.context, "User is successfully deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(view.context, "User is not deleted.", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun getAllUsersFromDatabase() {
        db = FirebaseFirestore.getInstance()
        db.collection("users").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(Korisnik::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun getUserData(callback: (result: MutableList<Korisnik>) -> Unit) {
        db = FirebaseFirestore.getInstance()
        var lista: MutableList<Korisnik> = mutableListOf()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    lista.add(Korisnik(
                        document.data["email"].toString(),
                        document.data["id"].toString(),
                     //   document.data["password"].toString(),
                        (document.data["isAdmin"].toString().toBoolean())))

                    //  Log.d(TAG, "${document.id} => ${document.data}")

                }
                println("********" + lista.size + "******")
                callback(lista)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                println("---------Greska baze--------------------")
            }
    }

}