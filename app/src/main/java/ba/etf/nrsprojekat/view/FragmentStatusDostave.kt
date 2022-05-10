package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService

class FragmentStatusDostave(product : Product) : Fragment(R.layout.promijeni_status_dostave_fragment) {
    private lateinit var naziv : TextView
    private lateinit var poslovnica : TextView
    private lateinit var kolicina : TextView
    private lateinit var status : TextView
    private lateinit var btnPrimljeni : Button
    val proizvod = product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        naziv = view.findViewById(R.id.proizvodDostava1)
        poslovnica = view.findViewById(R.id.poslovnicaDostava1)
        kolicina = view.findViewById(R.id.kolicinaDostava1)
        status = view.findViewById(R.id.statusDostava1)

        naziv.text = proizvod.name
        poslovnica.text = proizvod.poslovnicaName
        kolicina.text= proizvod.quantity.toString()
        status.text = proizvod.status

        btnPrimljeni = view.findViewById(R.id.btnPrimljeni)
        btnPrimljeni.setOnClickListener{
            ProductsService.addToReceived(naziv.text.toString(),poslovnica.text.toString(),kolicina.text.toString().toInt(),status.text.toString(),{})
        }
    }
}