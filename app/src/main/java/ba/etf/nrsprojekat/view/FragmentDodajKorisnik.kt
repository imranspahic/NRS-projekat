package ba.etf.nrsprojekat.view

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.services.LoginService
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.regex.Pattern


class FragmentDodajKorisnik : Fragment() {
    private lateinit var spiner: Spinner
    private lateinit var unosEmail: EditText
    private lateinit var unosLozinka: EditText
    private lateinit var btnDodaj: Button
    private lateinit var dbref: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dodaj_korisnik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spiner = view.findViewById(R.id.spiner)
        unosEmail = view.findViewById(R.id.unosEmail)
        unosLozinka = view.findViewById(R.id.unosLozinka)
        btnDodaj = view.findViewById(R.id.btnDodajKorisnik)
        var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")
        btnDodaj.setOnClickListener {
            val admin: Boolean
            if(spiner.selectedItem.toString().equals("Admin")) admin = true else admin = false
           // dodajUBazu(unosEmail.text.toString(), unosLozinka.text.toString(), admin) {}
        }

    }
    /*
    private fun dodajUBazu(email: String, password: String, isAdmin: Boolean, callback: (result: Boolean) -> Unit) {
      //  println("U BAZA FUNK")
        val user = hashMapOf(
            "email" to email,
            "password" to password ,
            "isAdmin" to isAdmin
        )

        dbref = FirebaseFirestore.getInstance()
        dbref.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
               // println("DODAO JE")
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
            //    println("NIJE DODAO")
                Log.w(TAG, "Error adding document", e)
                callback(false)
            }
    }  */
    }



