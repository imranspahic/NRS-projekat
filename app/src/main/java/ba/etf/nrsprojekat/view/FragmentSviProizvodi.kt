package ba.etf.nrsprojekat.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService


class FragmentSviProizvodi : Fragment(R.layout.svi_proizvodi_fragment) {
    private lateinit var recyclerViewAll: RecyclerView
    private lateinit var swipeRefreshLayout3: SwipeRefreshLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAll = view.findViewById(R.id.recyclerSviProizvodi)
        recyclerViewAll.layoutManager = LinearLayoutManager(view.context)

        if(PdvCategoriesService.pdvCategories.isEmpty()) {
                PdvCategoriesService.fetchPdvCategories {
                    if(ProductsService.products.isEmpty()) {
                        ProductsService.fetchProducts {  }
                    }
                }
        }

        ProductsService.getDeliveryProducts { result ->
            val myAdapter = DostavaAdapter(result)
            recyclerViewAll.adapter = myAdapter
        }
        swipeRefreshLayout3 = view.findViewById(R.id.swipeRefreshLayout3)
        swipeRefreshLayout3.setOnRefreshListener {
            ProductsService.getDeliveryProducts { result ->
                val myAdapter = DostavaAdapter(result)
                recyclerViewAll.adapter = myAdapter
            }
            recyclerViewAll.adapter!!.notifyDataSetChanged()
            swipeRefreshLayout3.isRefreshing = false
        }
    }
}