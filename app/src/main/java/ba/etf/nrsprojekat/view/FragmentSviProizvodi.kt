package ba.etf.nrsprojekat.view

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.snackbar.Snackbar


class FragmentSviProizvodi : Fragment(R.layout.svi_proizvodi_fragment) {
    private lateinit var recyclerViewAll : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerViewAll = view.findViewById(R.id.recyclerSviProizvodi)
        recyclerViewAll.layoutManager = LinearLayoutManager(view.context)

        ProductsService.getDeliveryProducts { result ->
            val myAdapter = DostavaAdapter(result)
            recyclerViewAll.adapter = myAdapter
        }

    }
}