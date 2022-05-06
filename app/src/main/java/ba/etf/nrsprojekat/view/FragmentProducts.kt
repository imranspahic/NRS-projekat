package ba.etf.nrsprojekat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService

class FragmentProducts : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_products, container, false)
        ProductsService.fetchProducts() {v ->

        }
        return view
    }

}