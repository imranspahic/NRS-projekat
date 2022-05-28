package ba.etf.nrsprojekat.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddUserActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar


class FragmentOpcijeAdmin : Fragment() {
    private lateinit var dodajDugme: MaterialButton
    private lateinit var recyclerGlavni: RecyclerView
    private lateinit var brojKorisnikaLabel: TextView
    private lateinit var userListAdapter: KorisnikAdapter

    private var addUserActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("OK", "Okej")
        if (result.resultCode == Activity.RESULT_OK) {
            val mode = result.data!!.getStringExtra("mode")
            var message = ""
            message = if(mode == "ADD") {
                "Korisnik uspješno dodan!"
            } else {
                "Korisnik uspješno ažuriran!"
            }
            Log.d("korisnik", "result ok")
            Snackbar.make(recyclerGlavni, message, Snackbar.LENGTH_LONG)
                .setAction("OK") { }
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_admin_opcije, container, false)

        dodajDugme = view.findViewById(R.id.addUserDugme)
        recyclerGlavni = view.findViewById(R.id.recyclerListaGlavni)
        brojKorisnikaLabel = view.findViewById(R.id.brojKorisnika)
        recyclerGlavni.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        userListAdapter = KorisnikAdapter(
            UserService.users,
            requireActivity(),
            addUserActivityLauncher,
            requireContext(),
            brojKorisnikaLabel
        )
        recyclerGlavni.adapter = userListAdapter
        brojKorisnikaLabel.setText(UserService.users.size.toString())

        UserService.fetchUsers() { result ->
            if(result) {
                userListAdapter.updateUsers(UserService.users)
            }
        }

        dodajDugme.setOnClickListener {
            otvoriDodavanjeKorisnika()
        }

        return view
    }

    private fun otvoriDodavanjeKorisnika() {
        val intent = Intent(requireActivity(), AddUserActivity::class.java)
        addUserActivityLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        userListAdapter.updateUsers(UserService.users)
    }
}