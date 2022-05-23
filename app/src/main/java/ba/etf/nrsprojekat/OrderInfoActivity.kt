package ba.etf.nrsprojekat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.view.CheckoutAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import java.text.DateFormat
import kotlin.math.roundToInt

class OrderInfoActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var iznosZaPlatiti : TextView
    private lateinit var listaRecycler : RecyclerView
    private lateinit var adapter : CheckoutAdapter
    private var iznos : Double = 0.0
    private lateinit var datum: TextView
    private lateinit var lokacija: TextView
    private lateinit var mjesto: TextView
    private lateinit var orderLoader: CircularProgressIndicator
    private lateinit var linearLayoutRacun: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)
        val orderID = intent.getStringExtra("OrderID")
        toolbar = findViewById(R.id.racunToolbar)
        iznosZaPlatiti = findViewById(R.id.iznosZaPlatiti)
        listaRecycler = findViewById(R.id.listaNarucenihArtikala)
        datum = findViewById(R.id.datumRacun)
        lokacija = findViewById(R.id.lokacijaRacun)
        mjesto = findViewById(R.id.stoRacun)
        orderLoader = findViewById(R.id.orderLoader)
        linearLayoutRacun = findViewById(R.id.linearLayoutZaRacun)

        orderLoader.visibility = View.VISIBLE
        orderLoader.progress = 10

        iznosZaPlatiti.visibility = View.GONE
        listaRecycler.visibility = View.GONE
        linearLayoutRacun.visibility = View.GONE


        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

       listaRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        OrderServices.getOrder(orderID, {
            var lista: MutableList<Narudzba>
            if (!it[0].isDeleted) {
                lista = it;
                datum.text = DateFormat.getDateInstance().format(it[0].datumNarucivanja)
                mjesto.text = it[0].mjesto
                lokacija.text = it[0]?.lokacija ?: "/"
                var brojProizvoda = lista[0].proizvodi.size
                var proizvodiList = lista[0].proizvodi
                //for (proizvod in proizvodiList)
                adapter = CheckoutAdapter(proizvodiList, lista)
                listaRecycler.adapter = adapter
                OrderServices.getFinalPriceByOrder(orderID, {
                    if (it != 0.0) {
                        iznos = it;
                        iznos = (iznos * 100.0).roundToInt() / 100.0
                        iznosZaPlatiti.text = iznos.toString() + " KM"
                    }
                   orderLoader.visibility = View.GONE

                   iznosZaPlatiti.visibility = View.VISIBLE
                    listaRecycler.visibility = View.VISIBLE
                   linearLayoutRacun.visibility = View.VISIBLE
                },
                    {
                        orderLoader.visibility = View.GONE
                        Snackbar.make(listaRecycler, "Greška!", Snackbar.LENGTH_LONG)
                            .setAction("OK") { }
                            .show()
                    }
                )
            }
        },
            {
                orderLoader.visibility = View.GONE
                Snackbar.make(listaRecycler, "Greška!", Snackbar.LENGTH_LONG)
                    .setAction("OK") { }
                    .show()
            }
        )
    }

    private fun onToolbarBackButton() {
        finish()
    }
}