package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

class AddBranchActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nazivPoslovnice: TextView
    private lateinit var nazivMjesta: TextView
    private lateinit var btnDodajPoslovnicu : Button
    private lateinit var btnDodajMjesto : Button
    private lateinit var listaMjestaPoslovnice: MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)
        toolbar = findViewById(R.id.addBranchToolbar)
        nazivPoslovnice = findViewById(R.id.unosPoslovnice)
        nazivMjesta = findViewById(R.id.unosMjesta)
        btnDodajMjesto = findViewById(R.id.dodajMjesto)
        listaMjestaPoslovnice = mutableListOf<String>()
        btnDodajPoslovnicu = findViewById(R.id.btnDodajPoslovnicu)
        val productID: String? = intent.getStringExtra("productID")

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
        btnDodajMjesto.setOnClickListener{
            listaMjestaPoslovnice.add(nazivMjesta.text.toString())
            showSnackBar("Mjesto uspješno dodano!", this)
        }

        btnDodajPoslovnicu.setOnClickListener {
            BranchesService.addBranch(productID ?: "",nazivPoslovnice.text.toString(),listaMjestaPoslovnice){
                    result, mode ->
                if(result) {
                    val output = Intent().apply {
                        putExtra("branches", mode)
                    }
                    setResult(Activity.RESULT_OK, output)
                    finish()
                }
            }
        }
    }
    private fun onToolbarBackButton() {
        finish()
    }
    fun showSnackBar(message: String?, activity: Activity?) {
        if (null != activity && null != message) {
            Snackbar.make(
                activity.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}