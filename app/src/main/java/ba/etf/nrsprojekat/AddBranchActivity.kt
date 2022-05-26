package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.text.DecimalFormat


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
        val branchID: String? = intent.getStringExtra("branches")


        btnDodajPoslovnicu.isEnabled = false

        if (branchID != null) {
            Log.d("branches", "branchID nije null, ažuriranje poslovnice")
            initializeProductData(branchID)
        }

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
        btnDodajMjesto.setOnClickListener{
            listaMjestaPoslovnice.add(nazivMjesta.text.toString())
            showSnackBar("Mjesto uspješno dodano!", this)
        }

        btnDodajPoslovnicu.setOnClickListener {
            onAddBranch(branchID)
        }

        nazivPoslovnice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState()
            }
        })
        nazivMjesta.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState()
            }
        })
    }
    private fun checkButtonState() {
        var branchNameCondition = nazivPoslovnice.text?.isNotEmpty() ?: false
        var branchPlaceCondition = nazivMjesta.text?.isNotEmpty() ?: false

        btnDodajPoslovnicu.isEnabled =
            branchNameCondition && branchPlaceCondition
    }

    private fun onToolbarBackButton() {
        finish()
    }

    private fun onAddBranch(branchID: String?) {
        BranchesService.addBranch(
            branchID ?: "",
            nazivPoslovnice.text.toString(),
            listaMjestaPoslovnice

        ) { result, mode ->
            if (result) {
                val output = Intent().apply {
                    putExtra("mode", mode)
                }
                setResult(Activity.RESULT_OK, output)
                finish()
            }
        }
    }

    private fun initializeProductData(productID: String) {
        val branch: Branch = BranchesService.branches.firstOrNull { branch -> branch.id == productID } ?: return
        nazivPoslovnice.setText(branch.nazivPoslovnice)
        nazivMjesta.setText(branch.mjesto.toString())
        toolbar.title = "Ažuriraj proizvod"
        btnDodajMjesto.isVisible = false
        btnDodajPoslovnicu.text = "SAČUVAJ"

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