package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firestore.v1.StructuredQuery

class LocationPickBottomSheetFragment()  : BottomSheetDialogFragment() {
    private lateinit var nazivPoslovnice : TextView
    private lateinit var mjestaPoslovniceSpiner : Spinner
    private lateinit var dodajLokacijuDugme : Button
    private lateinit var zatvoriLokacijuDugme : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.location_pick_bottom_sheet, container, false)

        nazivPoslovnice = view.findViewById(R.id.nazivPoslovniceText)
        mjestaPoslovniceSpiner = view.findViewById(R.id.mjestaPoslovniceSpiner)
        dodajLokacijuDugme = view.findViewById(R.id.DodajLokacijuDugme)
        zatvoriLokacijuDugme = view.findViewById(R.id.ZatvoriLokacijuDugme)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nazivPoslovnice.text = LoginService.logovaniKorisnik?.poslovnica()

        var mjestaZaPoslovnicu = mutableListOf<String>()

        BranchesService.getMjesta(nazivPoslovnice.text.toString()) {
            for(item in it.mjesto) mjestaZaPoslovnicu.add(item)
            val adapterZaSpiner = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                mjestaZaPoslovnicu
            )
            mjestaPoslovniceSpiner.adapter = adapterZaSpiner
        }



        zatvoriLokacijuDugme.setOnClickListener {
            dismiss()
        }
        dodajLokacijuDugme.setOnClickListener {
            OrderServices.lokacija = nazivPoslovnice.text.toString()
            OrderServices.mjesto = mjestaPoslovniceSpiner.selectedItem.toString()
            dismiss()
        }

    }

    companion object {
        const val TAG = "LocationPickBottomSheet"
    }

    private fun postaviSpiner() : ArrayList<String> {
        var nizLokacija = ArrayList<String>()
        BranchesService.getBranches { it ->
            for(item in it) nizLokacija.add(item.nazivPoslovnice)
        }
        return nizLokacija
    }

}