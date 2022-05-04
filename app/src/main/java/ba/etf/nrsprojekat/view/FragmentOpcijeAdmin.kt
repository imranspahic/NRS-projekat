package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ba.etf.nrsprojekat.MainActivity2
import ba.etf.nrsprojekat.R


class FragmentOpcijeAdmin : Fragment(R.layout.fragment_admin_opcije) {
    private lateinit var btnIzmijeni: Button
    private lateinit var btnDodaj: Button
    private lateinit var btnIzbrisi: Button

 /*   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_opcije, container, false)
    } */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnIzmijeni = view.findViewById(R.id.btnOpcijaIzmijeni)
        btnIzmijeni.setOnClickListener {
            (activity as MainActivity2?)?.openFragment(FragmentAdmin())
        }
        btnDodaj = view.findViewById(R.id.btnOpcijaDodaj)
        btnDodaj.setOnClickListener {
            (activity as MainActivity2?)?.openFragment(FragmentDodajKorisnik())

        }
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