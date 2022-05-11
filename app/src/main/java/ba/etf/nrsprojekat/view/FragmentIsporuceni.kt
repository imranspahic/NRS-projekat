package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService

class FragmentIsporuceni : Fragment(R.layout.isporuceni_proizvodi_fragment) {
    private lateinit var recyclerViewDelivered: RecyclerView
    private lateinit var deliveredAdapter: DeliveredAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerViewDelivered = view.findViewById(R.id.recyclerIsporuceni)
        recyclerViewDelivered.layoutManager = LinearLayoutManager(view.context)
        deliveredAdapter = DeliveredAdapter(mutableListOf())

        ProductsService.getDeliveredProducts { result ->
            val myAdapter = DeliveredAdapter(result)
            recyclerViewDelivered.adapter = myAdapter
        }
    }
    override fun onResume() {
        super.onResume()
        deliveredAdapter.updateProducts(ProductsService.lista3)
    }

}