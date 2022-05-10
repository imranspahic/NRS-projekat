package ba.etf.nrsprojekat.view

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.AddProductActivity
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar


class FragmentProducts : Fragment() {

    private lateinit var brojProizvodaText: TextView
    private lateinit var refreshDugme: MaterialButton
    private lateinit var addDugme: MaterialButton
    private lateinit var proizvodiRecyclerView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var addOrderDugme: MaterialButton
    private lateinit var discardOrderDugme: MaterialButton
    private lateinit var saveOrderDugme: MaterialButton
    private lateinit var brojProizvodaLabel: TextView

    private var productActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val mode = result.data!!.getStringExtra("mode")
            var message = ""
            message = if(mode == "ADD") {
                "Proizvod uspješno dodan!"
            } else {
                "Proizvod uspješno ažuriran!"
            }
            Log.d("products", "result ok")
            Snackbar.make(proizvodiRecyclerView, message, Snackbar.LENGTH_LONG)
                .setAction("OK") { }
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_products, container, false)
        brojProizvodaLabel = view.findViewById(R.id.brojProizvodaLabel)
        brojProizvodaText = view.findViewById(R.id.brojProizvoda)
        refreshDugme = view.findViewById(R.id.refreshProductDugme)
        addDugme = view.findViewById(R.id.addProductDugme)
        proizvodiRecyclerView = view.findViewById(R.id.proizvodiRecyclerView)
        addOrderDugme = view.findViewById(R.id.addOrderDugme)
        discardOrderDugme = view.findViewById(R.id.discardOrderDugme)
        saveOrderDugme = view.findViewById(R.id.saveOrderDugme)
        addOrderDugme.visibility = View.GONE
        discardOrderDugme.visibility = View.GONE
        saveOrderDugme.visibility = View.GONE
        proizvodiRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        productListAdapter = ProductListAdapter(
            ProductsService.products,
            requireContext(),
            requireActivity(),
            productActivityLauncher,
            brojProizvodaText
        )
        brojProizvodaText.text = ProductsService.products.size.toString()
        proizvodiRecyclerView.adapter = productListAdapter
        ProductsService.fetchProducts() {result ->
            if(result) {
                productListAdapter.updateProducts(ProductsService.products)
                brojProizvodaText.text = ProductsService.products.size.toString()
            }
        }

        refreshDugme.setOnClickListener {
            ProductsService.fetchProducts() {result ->
                if(result) {
                    productListAdapter.updateProducts(ProductsService.products)
                    brojProizvodaText.text = ProductsService.products.size.toString()
                }
            }
        }

        addDugme.setOnClickListener {
            otvoriDodavanjeProizvoda()
        }

        if(!LoginService.logovaniKorisnik!!.isAdmin()) {
            addDugme.visibility = View.GONE
            addOrderDugme.visibility = View.VISIBLE
        }

        return view
    }
    // ON VIEW CREATED ---------------------------------------------------------------

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOrderDugme.setOnClickListener {
            var imeNarudzbe = showdialog()
            discardOrderDugme.setOnClickListener {
                addOrderDugme.visibility = View.VISIBLE
                saveOrderDugme.visibility = View.GONE
                brojProizvodaLabel.text = "Broj proizvoda:"
                brojProizvodaText.text = ProductsService.products.size.toString()
                discardOrderDugme.visibility = View.GONE

            }
            saveOrderDugme.setOnClickListener {

            }


        }
    }

    private fun otvoriDodavanjeProizvoda() {
        val intent = Intent(activity, AddProductActivity::class.java)
        productActivityLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        productListAdapter.updateProducts(ProductsService.products)
        brojProizvodaText.text = ProductsService.products.size.toString()
    }
    fun showdialog(): String{
        var m_Text: String = String()
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Ime narudžbe")
        val input = EditText(requireContext())
        input.setHint("Unesite ime narudžbe")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Sačuvaj", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            m_Text = input.text.toString()
            addOrderDugme.visibility = View.GONE
            discardOrderDugme.visibility = View.VISIBLE
            saveOrderDugme.visibility = View.VISIBLE
            brojProizvodaLabel.text="Ime narudžbe: "
            brojProizvodaText.text=m_Text
        })
        builder.setNegativeButton("Odustani", DialogInterface.OnClickListener {
                dialog, which -> dialog.cancel()
            m_Text = "00000000"
        })
        builder.show()
        return m_Text
    }

}