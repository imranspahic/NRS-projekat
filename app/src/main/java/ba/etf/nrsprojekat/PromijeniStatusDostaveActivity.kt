package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class PromijeniStatusDostaveActivity :AppCompatActivity()  {

        private lateinit var toolbar2: MaterialToolbar

        private lateinit var nazivProizvoda: TextView
        private lateinit var nazivPoslovnice: TextView
        private lateinit var kolicina: TextView
        private lateinit var status: TextView
        private lateinit var btnPrimljeni : Button
        private lateinit var btnPoslani : Button
        private lateinit var btnIsporuceni : Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.promijeni_status_dostave_activity)
            val proizvod1 = intent.getStringExtra("Proizvodi").toString()
            val proizvod  = ProductsService.getReceivedName(proizvod1)

            toolbar2 = findViewById(R.id.toolbarStatusDostave)

            nazivProizvoda = findViewById(R.id.proizvodDostava1)
            nazivPoslovnice = findViewById(R.id.poslovnicaDostava1)
            kolicina = findViewById(R.id.kolicinaDostava1)
            status = findViewById(R.id.statusDostava1)

            nazivProizvoda.text = proizvod.name
            nazivPoslovnice.text = proizvod.poslovnicaName
            kolicina.text= proizvod.quantity.toString()
            status.text = proizvod.status


            toolbar2.setNavigationOnClickListener {
                onToolbarBackButton()
            }

            btnPrimljeni = findViewById(R.id.btnPrimljeni)
            btnPrimljeni.setOnClickListener{
                ProductsService.addToReceived(nazivProizvoda.text.toString(),nazivPoslovnice.text.toString(),kolicina.text.toString().toInt(),status.text.toString(),{})
                btnPoslani.setEnabled(false)
                btnIsporuceni.setEnabled(false)
            }

            btnPoslani = findViewById(R.id.btnPoslani)
            btnPoslani.setOnClickListener{
                ProductsService.addToSent(nazivProizvoda.text.toString(),nazivPoslovnice.text.toString(),kolicina.text.toString().toInt(),status.text.toString(),{})
                btnPrimljeni.setEnabled(false)
                btnIsporuceni.setEnabled(false)
            }
            btnIsporuceni = findViewById(R.id.btnIsporuceni)
            btnIsporuceni.setOnClickListener{
                ProductsService.addToDelivered(nazivProizvoda.text.toString(),nazivPoslovnice.text.toString(),kolicina.text.toString().toInt(),status.text.toString(),{})
                btnPoslani.setEnabled(false)
                btnPrimljeni.setEnabled(false)
            }
        }
        private fun onToolbarBackButton() {
            finish()
        }

    }

