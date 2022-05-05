package ba.etf.nrsprojekat.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FragmentAdmin : Fragment(R.layout.fragment_admin), KorisnikAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var dbref: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var listaKorisnik: List<Korisnik>
    private lateinit var emailTekst: EditText
    private lateinit var lozinkaTekst: EditText
    private lateinit var dugme: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView =  view.findViewById(R.id.recyclerLista)
        emailTekst = view.findViewById(R.id.emailTekst)
        lozinkaTekst = view.findViewById(R.id.lozinkaTekst)
        dbref = FirebaseFirestore.getInstance()
        dugme = view.findViewById(R.id.dugmeIzmijeni)
        dugme.isEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        getUserData {
            listaKorisnik2 -> if(listaKorisnik2 != null) {
               var adapterZaRecycler = KorisnikAdapter(listaKorisnik2, this)
               listaKorisnik = listaKorisnik2
               recyclerView.adapter = adapterZaRecycler
        }
        }
        dugme.setOnClickListener {
            izmijeniSifru(emailTekst.text.toString(), lozinkaTekst.text.toString()) {}

        }
    }

    override fun onItemClick(position: Int) {
            //Toast.makeText( "Korisnici ne postoje!", Toast.LENGTH_SHORT)
            dugme.isEnabled = true
            emailTekst.setText(listaKorisnik[position].getEmail().toString())
            lozinkaTekst.setText(listaKorisnik[position].getPassword().toString())

    }

    private fun getUserData(callback: (result: MutableList<Korisnik>) -> Unit) {
        var lista: MutableList<Korisnik> = mutableListOf()
        dbref.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.data["isAdmin"].toString().toBoolean() == false)
                    lista.add(Korisnik(
                        document.data["id"].toString(),
                        document.data["email"].toString(),
                        document.data["password"].toString(),
                        (document.data["isAdmin"].toString().toBoolean())))
                  //  Log.d(TAG, "${document.id} => ${document.data}")
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun izmijeniSifru(email: String, novaSifra: String, callback: (result: Boolean) -> Unit) {
        var map = mutableMapOf<String, String>()
        map["password"] = novaSifra
       dbref.collection("users").whereEqualTo("email", email).get().addOnSuccessListener {
           QuerySnapshot ->
           if(QuerySnapshot.documents.isNotEmpty()) {
               for(person in QuerySnapshot)
                   dbref.collection("users").document(person.id).set(map, SetOptions.merge())
       }
           callback(true)
       }
    }
}