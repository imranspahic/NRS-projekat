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

        brojProizvodaText = view.findViewById(R.id.brojProizvoda)
        refreshDugme = view.findViewById(R.id.refreshProductDugme)
        addDugme = view.findViewById(R.id.addProductDugme)
        proizvodiRecyclerView = view.findViewById(R.id.proizvodiRecyclerView)
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
        }

        return view
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

}