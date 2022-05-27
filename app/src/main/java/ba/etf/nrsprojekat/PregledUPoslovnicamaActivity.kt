package ba.etf.nrsprojekat

import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.view.ProizvodiUPoslovniciAdapter
import com.google.android.material.appbar.MaterialToolbar

class PregledUPoslovnicamaActivity  : AppCompatActivity()  {
    private lateinit var proizvod: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var productNameP: TextView
    private lateinit var kolicina: TextView
    private lateinit var status: TextView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proizvodi_u_poslovnicama)

        proizvod = findViewById(R.id.ProizvodiUPoslovnici)
        toolbar = findViewById(R.id.poslovnicaProizvodiToolbar)
        emptyView = findViewById(R.id.natpisNemaProizvoda)
        proizvod.layoutManager = LinearLayoutManager(applicationContext)
        val proizvod1 = intent.getStringExtra("Poslovnice").toString()
        toolbar.title = "Poslovnica: $proizvod1"

        val adapter = ProizvodiUPoslovniciAdapter(mutableListOf())
        proizvod.adapter = adapter

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        if(PdvCategoriesService.pdvCategories.isEmpty()) {
            PdvCategoriesService.fetchPdvCategories {
            }
        }

        ProductsService.FilterProducts(proizvod1){
            println(it)
            val productNiz = it
            println(productNiz)
            adapter.updateProizvodi(productNiz)
            Log.d("poslovnice", "broj proizvoda = ${productNiz.size}")
            if(it.size == 0){
                proizvod.setVisibility(INVISIBLE);
                emptyView.setVisibility(VISIBLE);
            }
            else {
                proizvod.setVisibility(VISIBLE);
                emptyView.setVisibility(INVISIBLE);
            }

        }
    }

    private fun onToolbarBackButton() {
        finish()
    }

}