package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.view.Adapter
import com.google.firebase.firestore.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var userArrayList: ArrayList<Korisnik>
    private lateinit var myAdapter : Adapter
    private lateinit var db : FirebaseFirestore
    private lateinit var btnDelete : Button
    private lateinit var email : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()
        myAdapter = Adapter(userArrayList)
        recyclerView.adapter = myAdapter

        getAllUsersFromDatabase()

        btnDelete = findViewById(R.id.deleteUser)
        email = findViewById(R.id.inputEmail)

        btnDelete.setOnClickListener {
            if (email.text.isNotEmpty()) {
                val query = db.collection("users")
                    .whereEqualTo("email", email.text.toString())
                    .get()
                query.addOnSuccessListener {
                    for (document in it) {
                        db.collection("users").document(document.id).delete()
                        Toast.makeText(applicationContext, "User is successfully deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "User is not deleted.", Toast.LENGTH_SHORT).show()
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

}
