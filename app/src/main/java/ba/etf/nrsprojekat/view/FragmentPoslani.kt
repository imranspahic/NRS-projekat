package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService

class FragmentPoslani : Fragment(R.layout.poslani_proizvodi_fragment) {
    private lateinit var recyclerViewSent: RecyclerView
    private lateinit var sentAdapter: SentAdapter
    private lateinit var swipeRefreshLayout1: SwipeRefreshLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerViewSent = view.findViewById(R.id.recyclerPoslani)
        recyclerViewSent.layoutManager = LinearLayoutManager(view.context)
        sentAdapter = SentAdapter(mutableListOf())

        ProductsService.getSentProducts { result ->
            val myAdapter = SentAdapter(result)
            recyclerViewSent.adapter = myAdapter
        }
        swipeRefreshLayout1 = view.findViewById(R.id.swipeRefreshLayout1)
        swipeRefreshLayout1.setOnRefreshListener {
            ProductsService.getSentProducts { result ->
                val myAdapter = SentAdapter(result)
                recyclerViewSent.adapter = myAdapter
            }
            recyclerViewSent.adapter!!.notifyDataSetChanged()
            swipeRefreshLayout1.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        sentAdapter.updateProducts(ProductsService.lista2)
    }
}