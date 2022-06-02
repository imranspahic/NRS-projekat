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
    private lateinit var brojRacunaLinear: LinearLayout
    private lateinit var datumRacunaLinear: LinearLayout
    private lateinit var vrijemeRacunaLinear: LinearLayout
    private lateinit var brojRacuna: TextView
    private lateinit var datumRacuna: TextView
    private lateinit var vrijemeRacuna: TextView
    private lateinit var separator1: View
    private lateinit var separator2: View

    private lateinit var narudzba: Narudzba

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
        brojRacunaLinear = findViewById(R.id.brojRacunaLinear)
        datumRacunaLinear = findViewById(R.id.datumRacunaLinear)
        vrijemeRacunaLinear = findViewById(R.id.vrijemeRacunaLinear)
        brojRacuna = findViewById(R.id.brojRacuna)
        datumRacuna = findViewById(R.id.datumRacuna)
        vrijemeRacuna = findViewById(R.id.vrijemeRacuna)
        separator1 = findViewById(R.id.racunSeparator)
        separator2 = findViewById(R.id.racunSeparator1)


        orderLoader.visibility = View.VISIBLE
        orderLoader.progress = 10

        iznosZaPlatiti.visibility = View.GONE
        listaRecycler.visibility = View.GONE
        linearLayoutRacun.visibility = View.GONE
        brojRacunaLinear.visibility = View.GONE
        datumRacunaLinear.visibility = View.GONE
        vrijemeRacunaLinear.visibility = View.GONE
        separator1.visibility = View.GONE
        separator2.visibility = View.GONE




        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

       listaRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        OrderServices.getOrder(orderID, {
            narudzba, ukupnaCijena ->
            this.narudzba = narudzba
                datum.text = DateFormat.getDateInstance().format(narudzba.datumNarucivanja)
                mjesto.text = if (narudzba.mjesto == "null") "-" else narudzba.mjesto
                lokacija.text = if (narudzba.lokacija == "null") "-" else narudzba.lokacija
                var brojProizvoda = narudzba.proizvodi.size
                var proizvodiList = narudzba.proizvodi


                adapter = CheckoutAdapter(proizvodiList, listOf(narudzba))

                listaRecycler.adapter = adapter
                    if (ukupnaCijena != 0.0) {
                        iznos = ukupnaCijena;
                        iznos = (iznos * 100.0).roundToInt() / 100.0
                        iznosZaPlatiti.text = String.format("%.2f", iznos).replace(".",",") + " KM"
                    }

                    if(narudzba.brojRacuna == null) {
                        Log.d("orders", "Dohvacanje broja racuna ...")
                        OrderServices.fiskalizirajRacun(narudzba, {
                            Log.d("orders", "Error callback pozvan")
                            orderLoader.visibility = View.GONE
                            iznosZaPlatiti.visibility = View.VISIBLE
                            listaRecycler.visibility = View.VISIBLE
                            linearLayoutRacun.visibility = View.VISIBLE
                            Snackbar.make(listaRecycler, it, Snackbar.LENGTH_LONG)
                                .setAction("OK") { }
                                .show()

                        }, {brojRacuna, datumRacuna, vrijemeRacuna ->
                            Log.d("orders", "Success callback pozvan")
                            OrderServices.azurirajRacunInformacije(narudzba, brojRacuna, datumRacuna, vrijemeRacuna)
                            orderLoader.visibility = View.GONE
                            iznosZaPlatiti.visibility = View.VISIBLE
                            listaRecycler.visibility = View.VISIBLE
                            linearLayoutRacun.visibility = View.VISIBLE

                            brojRacunaLinear.visibility = View.VISIBLE
                            datumRacunaLinear.visibility = View.VISIBLE
                            vrijemeRacunaLinear.visibility = View.VISIBLE
                            separator1.visibility = View.VISIBLE
                            separator2.visibility = View.VISIBLE

                            this.brojRacuna.text = brojRacuna.toString()
                            this.datumRacuna.text = datumRacuna
                            this.vrijemeRacuna.text = vrijemeRacuna
                        })
                    }
                     else {
                         orderLoader.visibility = View.GONE
                         iznosZaPlatiti.visibility = View.VISIBLE
                         listaRecycler.visibility = View.VISIBLE
                         linearLayoutRacun.visibility = View.VISIBLE
                        brojRacunaLinear.visibility = View.VISIBLE
                        datumRacunaLinear.visibility = View.VISIBLE
                        vrijemeRacunaLinear.visibility = View.VISIBLE
                        separator1.visibility = View.VISIBLE
                        separator2.visibility = View.VISIBLE

                        brojRacuna.text = narudzba.brojRacuna.toString()
                        datumRacuna.text = narudzba.datumRacuna
                        vrijemeRacuna.text = narudzba.vrijemeRacuna
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