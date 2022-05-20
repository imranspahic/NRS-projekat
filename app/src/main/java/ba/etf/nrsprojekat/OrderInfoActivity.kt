package ba.etf.nrsprojekat

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.services.OrderServices
import ba.etf.nrsprojekat.view.CheckoutAdapter
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.roundToInt

class OrderInfoActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var iznosZaPlatiti : TextView
    private lateinit var listaRecycler : RecyclerView
    private lateinit var adapter : CheckoutAdapter
    private var iznos : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)
        val orderID = intent.getStringExtra("OrderID")
        toolbar = findViewById(R.id.racunToolbar)
        iznosZaPlatiti = findViewById(R.id.iznosZaPlatiti)
        listaRecycler = findViewById(R.id.listaNarucenihArtikala)
        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
        listaRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        OrderServices.getFinalPriceByOrder(orderID) {
            if (it != 0.0) {
                iznos = it;
                iznos = (iznos * 100.0).roundToInt() / 100.0
                iznosZaPlatiti.text = iznos.toString() + " KM"
            }
        }
        OrderServices.getOrder(orderID) {
            var lista: MutableList<Narudzba>
            if (!it[0].isDeleted) {
                lista = it;
                var brojProizvoda = lista[0].proizvodi.size
                var proizvodiList = lista[0].proizvodi
                //for (proizvod in proizvodiList)
                adapter = CheckoutAdapter(proizvodiList)
                listaRecycler.adapter = adapter
                //Log.d("oki", proizvod.toString())
                //}
            }
        }
    }

    private fun onToolbarBackButton() {
        finish()
    }
}