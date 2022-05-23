package ba.etf.nrsprojekat

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.view.ProizvodiUPoslovniciAdapter

class PregledUPoslovnicamaActivity  : AppCompatActivity()  {
    private lateinit var proizvod: RecyclerView
    private lateinit var emptyView: TextView



    private lateinit var productNameP: TextView
    private lateinit var kolicina: TextView
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proizvodi_u_poslovnicama)

        proizvod = findViewById(R.id.ProizvodiUPoslovnici)
        emptyView = findViewById(R.id.natpisNemaProizvoda)
        proizvod.layoutManager = LinearLayoutManager(applicationContext)
        val proizvod1 = intent.getStringExtra("Poslovnice").toString()
        val proizvod2 = ProductsService.FilterProducts(proizvod1){
                it ->
            println(it)

            val productNiz = it
            println(productNiz)
            val adapter = ProizvodiUPoslovniciAdapter(productNiz)
            proizvod.adapter = adapter
            if(it.size == 0){
                proizvod.setVisibility(INVISIBLE);
                emptyView.setVisibility(VISIBLE);

            }
            else {
                proizvod.setVisibility(VISIBLE);
                emptyView.setVisibility(INVISIBLE);
            }


        }


        /* productNameP= findViewById(R.id.productNamePoslovnica)
         kolicina = findViewById(R.id.productQuantityPoslovnica)
         status = findViewById(R.id.productStatusPoslovnica)


         //for()
         productNameP.text = "ime"
         kolicina.text= "kolicina"
         status.text = "status"
 */



    }


}