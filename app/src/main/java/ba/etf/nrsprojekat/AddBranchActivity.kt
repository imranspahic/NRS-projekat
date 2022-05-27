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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.models.Branch
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import ba.etf.nrsprojekat.view.MjestoListAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.RoundingMode
import java.text.DecimalFormat


class AddBranchActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var nazivPoslovniceText: TextView
    private lateinit var btnDodajMjesto : MaterialButton
    private lateinit var btnSacuvaj : Button
    private lateinit var recyclerMjesto : RecyclerView
    private lateinit var nazivPoslovniceLayout : TextInputLayout
    private lateinit var listaMjestaPoslovnice : MutableList<String>
    private lateinit var adapterZaRecycler : MjestoListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)
        toolbar = findViewById(R.id.addBranchToolbar)
        nazivPoslovniceLayout = findViewById(R.id.addBranchNameTextInput)
        nazivPoslovniceText = findViewById(R.id.unosPoslovnice)
        btnDodajMjesto = findViewById(R.id.addMjestoBtn)
        btnSacuvaj = findViewById(R.id.btnSacuvajMjesto)
        recyclerMjesto = findViewById(R.id.mjestaRecyclerView)
        listaMjestaPoslovnice = mutableListOf()
        var branchID: String? = intent.getStringExtra("branches")
        toolbar.title = "Poslovnica"

        BranchesService.mjesta = mutableListOf()
        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        if (branchID != null) {
            initializeBranchData(branchID)
        }


        else {
        }

           btnDodajMjesto.setOnClickListener {
               listaMjestaPoslovnice.add("")
               recyclerMjesto.layoutManager = LinearLayoutManager(this)
               adapterZaRecycler = MjestoListAdapter(listaMjestaPoslovnice)
               recyclerMjesto.adapter = adapterZaRecycler
           }

            btnSacuvaj.setOnClickListener {
                if (nazivPoslovniceText.text.isEmpty()) {
                    nazivPoslovniceLayout.isErrorEnabled = true
                    nazivPoslovniceLayout.error = "Unesite ime poslovnice!"
                } else {
                    nazivPoslovniceLayout.isErrorEnabled = false
                    /*  BranchesService.addBranch("", nazivPoslovniceText.text.toString(), mutableListOf()) {
                          result, mode ->
                      } */
                    if(branchID == null) {

                            BranchesService.addBranch(
                                "",
                                nazivPoslovniceText.text.toString(),
                              //  listaMjestaPoslovnice
                            BranchesService.mjesta
                            ) { result, more ->
                                BranchesService.getID(nazivPoslovniceText.text.toString()) {
                                    branchID = it
                                }
                            }
                    }
                    else {
                        BranchesService.addBranch(branchID!!, nazivPoslovniceText.text.toString(), /*listaMjestaPoslovnice*/ BranchesService.mjesta) {
                            result, more ->
                        }
                    }
                }
            }



        /*
           btnDodajMjesto.setOnClickListener {
                listaMjestaPoslovnice.add("")
                BranchesService.addBranch(branch.id, nazivPoslovniceText.text.toString(), listaMjestaPoslovnice) {
                    result, mode ->
                }
            }
         */
        /*
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
        } */
    }
    private fun onAddBranch(branchID: String?) {
        BranchesService.addBranch(
            branchID ?: "",
            nazivPoslovniceText.text.toString(),
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

    private fun onToolbarBackButton() {
        finish()
    }
    private fun initializeBranchData(branchID: String) {
        val branch: Branch = BranchesService.branches.firstOrNull { branch -> branch.id == branchID } ?: return
        nazivPoslovniceText.setText(branch.nazivPoslovnice)
        listaMjestaPoslovnice = branch.mjesto
        recyclerMjesto.layoutManager = LinearLayoutManager(this)
         adapterZaRecycler = MjestoListAdapter(branch.mjesto)
        recyclerMjesto.adapter = adapterZaRecycler
    }

}