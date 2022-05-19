package ba.etf.nrsprojekat.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton


class FragmentOrder() : Fragment() {
    private lateinit var btnDodajOrder: MaterialButton
    private lateinit var narudzbeRecyclerView: RecyclerView
    private lateinit var ordersList: List<Narudzba>
    private lateinit var brojNarudzbi: TextView
    private var productActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("OK", "Okej") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        narudzbeRecyclerView = view.findViewById(R.id.narudzbeRecyclerView)
        brojNarudzbi = view.findViewById(R.id.brojNarudzbi)
        narudzbeRecyclerView.layoutManager = LinearLayoutManager(view.context)
        OrderServices.getOrders(LoginService.logovaniKorisnik!!.getID().toString()) { it->
            ordersList = it
            brojNarudzbi.text = ordersList.size.toString()
            var adapterZaRecycler = OrderListAdapter(ordersList, requireContext(), requireActivity(), productActivityLauncher)
            narudzbeRecyclerView.adapter = adapterZaRecycler
            //println(OrderServices!!.imeTrenutneNarudzbe?.toString())

        }
    }





}