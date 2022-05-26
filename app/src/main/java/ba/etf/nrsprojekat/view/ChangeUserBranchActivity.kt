package ba.etf.nrsprojekat.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class ChangeUserBranchActivity : AppCompatActivity() {
    private lateinit var spinnerPoslovnica: Spinner
    private lateinit var changePoslovnicaSaveDugme: MaterialButton
    private lateinit var toolbar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_branch)
        spinnerPoslovnica = findViewById(R.id.spinnerPoslovnice)
        toolbar = findViewById(R.id.changePoslovnicaToolbar)
        changePoslovnicaSaveDugme = findViewById(R.id.changePoslovnicaSaveDugme)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        changePoslovnicaSaveDugme.setOnClickListener {
            onSavePoslovnica()
        }


        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, mutableListOf<String>())
        spinnerPoslovnica.adapter = adapter

        if(BranchesService.branches.isEmpty()) {
            BranchesService.getBranches {
                val poslovnice = mutableListOf<String>()
               BranchesService.branches.forEach { branch -> poslovnice.add(branch.nazivPoslovnice) }
                adapter.clear()
                adapter.addAll(poslovnice)
                adapter.notifyDataSetChanged()
                spinnerPoslovnica.setSelection(BranchesService.branches.indexOfFirst { branch -> branch.nazivPoslovnice == LoginService.logovaniKorisnik!!.poslovnica() })
            }
        }
        else {
            val poslovnice = mutableListOf<String>()
            BranchesService.branches.forEach { branch -> poslovnice.add(branch.nazivPoslovnice) }
            adapter.clear()
            adapter.addAll(poslovnice)
            adapter.notifyDataSetChanged()
            spinnerPoslovnica.setSelection(BranchesService.branches.indexOfFirst { branch -> branch.nazivPoslovnice == LoginService.logovaniKorisnik!!.poslovnica() })
        }
        Log.d("poslovnice", "poslovnica korisnika = ${LoginService.logovaniKorisnik!!.poslovnica()}")
    }

    private fun onToolbarBackButton() {
        finish()
    }

    private fun onSavePoslovnica() {
        UserService.updateUserPoslovnica(
            LoginService.logovaniKorisnik!!.getID(),
            spinnerPoslovnica.selectedItem as String
        ) {
            LoginService.logovaniKorisnik!!.poslovnica = spinnerPoslovnica.selectedItem as String
            finish()
        }
    }
}