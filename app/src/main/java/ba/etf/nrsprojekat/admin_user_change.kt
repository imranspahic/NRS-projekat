package ba.etf.nrsprojekat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.Korisnik
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [admin_user_change.newInstance] factory method to
 * create an instance of this fragment.
 */
class admin_user_change : Fragment(R.layout.fragment_admin_user_change), KorisnikAdapter.OnItemClickListener {
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
                    lista.add(Korisnik(document.data["email"].toString(), document.data["password"].toString(), (document.data["isAdmin"].toString().toBoolean())))
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment admin_user_change.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            admin_user_change().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}