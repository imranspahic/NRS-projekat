package ba.etf.nrsprojekat.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddUserActivity
import ba.etf.nrsprojekat.MainActivity2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.button.MaterialButton


class FragmentOpcijeAdmin : Fragment(R.layout.fragment_admin_opcije) {
  //  private lateinit var btnIzmijeni: Button
  //  private lateinit var btnDodaj: Button
  //  private lateinit var btnIzbrisi: Button
    private lateinit var dodajDugme: MaterialButton
    private lateinit var recyclerGlavni: RecyclerView
    private lateinit var listaKorisnik: List<Korisnik>
    private lateinit var brojKorisnikaLabel: TextView
    private var productActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("OK", "Okej")

    }

 /*   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_opcije, container, false)
    } */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dodajDugme = view.findViewById(R.id.addUserDugme)
        recyclerGlavni = view.findViewById(R.id.recyclerListaGlavni)
        brojKorisnikaLabel = view.findViewById(R.id.brojKorisnika)
        recyclerGlavni.layoutManager = LinearLayoutManager(view.context)
        UserService.getUserData { result ->
            listaKorisnik = result
            var adapterZaRecycler = KorisnikAdapter(listaKorisnik, requireActivity(), productActivityLauncher, requireContext())
            recyclerGlavni.adapter = adapterZaRecycler
            brojKorisnikaLabel.setText(listaKorisnik.size.toString())
        }


        dodajDugme.setOnClickListener {
            val intent = Intent(requireActivity(), AddUserActivity::class.java)
            intent.putExtra("Bool", "false")
            startActivity(intent)
        }

        /*
        btnIzmijeni = view.findViewById(R.id.btnOpcijaIzmijeni)
        btnIzmijeni.setOnClickListener {
            (activity as MainActivity2?)?.openFragment(FragmentAdmin())
        }
        btnDodaj = view.findViewById(R.id.btnOpcijaDodaj)
        btnDodaj.setOnClickListener {
            (activity as MainActivity2?)?.openFragment(FragmentDodajKorisnik())

        }
        btnIzbrisi = view.findViewById(R.id.btnOpcijaObrisi)
        btnIzbrisi.setOnClickListener {
            (activity as MainActivity2?)?.openFragment(FragmentObrisi())

        } */

        /*
        bottomNavigation = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener(mOnItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.admin
       // val adminFragment = FragmentAdmin()
       // openFragment(adminFragment)
        val opcijeFragment = FragmentOpcijeAdmin()
        openFragment(opcijeFragment)
         */
    }





 /*   companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentOpcijeAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    } */
}