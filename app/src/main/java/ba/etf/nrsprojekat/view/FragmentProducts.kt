package ba.etf.nrsprojekat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton

class FragmentProducts : Fragment() {

    private lateinit var brojProizvodaText: TextView
    private lateinit var refreshDugme: MaterialButton
    private lateinit var proizvodiRecyclerView: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_products, container, false)

        brojProizvodaText = view.findViewById(R.id.brojProizvoda)
        refreshDugme = view.findViewById(R.id.refreshProductDugme)
        proizvodiRecyclerView = view.findViewById(R.id.proizvodiRecyclerView)
        proizvodiRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        productListAdapter = ProductListAdapter(ProductsService.products)
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
        return view
    }

}