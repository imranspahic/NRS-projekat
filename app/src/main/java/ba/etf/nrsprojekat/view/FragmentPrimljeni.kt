package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.receivedProducts
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.button.MaterialButton

class FragmentPrimljeni : Fragment(R.layout.primljeni_proizvodi_fragment) {
    private lateinit var recyclerViewReceived: RecyclerView
    private lateinit var receivedAdapter: ReceivedAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewReceived = view.findViewById(R.id.recyclerPrimljeni)
        recyclerViewReceived.layoutManager = LinearLayoutManager(view.context)
        receivedAdapter = ReceivedAdapter(mutableListOf())

        ProductsService.getReceivedProducts { result ->
            val myAdapter = ReceivedAdapter(result)
            recyclerViewReceived.adapter = myAdapter
        }
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            ProductsService.getReceivedProducts { result ->
                val myAdapter = ReceivedAdapter(result)
                recyclerViewReceived.adapter = myAdapter
            }
            recyclerViewReceived.adapter!!.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }
    override fun onResume() {
        super.onResume()
        receivedAdapter.updateProducts(ProductsService.lista1)
    }
}