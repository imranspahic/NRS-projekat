package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.OrderServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firestore.v1.StructuredQuery

class LocationPickBottomSheetFragment()  : BottomSheetDialogFragment() {
    private lateinit var nazivPoslovniceSpiner : Spinner
    private lateinit var nazivLokacijeSpiner : Spinner
    private lateinit var dodajLokacijuDugme : Button
    private lateinit var zatvoriLokacijuDugme : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.location_pick_bottom_sheet, container, false)
        nazivPoslovniceSpiner = view.findViewById(R.id.nazivPoslovniceSpiner)
        nazivLokacijeSpiner = view.findViewById(R.id.nazivLokacijeSpiner)
        dodajLokacijuDugme = view.findViewById(R.id.DodajLokacijuDugme)
        zatvoriLokacijuDugme = view.findViewById(R.id.ZatvoriLokacijuDugme)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        zatvoriLokacijuDugme.setOnClickListener {
            dismiss()
        }
        dodajLokacijuDugme.setOnClickListener {
            OrderServices.lokacija = nazivPoslovniceSpiner.selectedItem.toString()
            OrderServices.mjesto = nazivLokacijeSpiner.selectedItem.toString()
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