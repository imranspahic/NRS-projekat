package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.util.regex.Pattern


class AddUserActivity : AppCompatActivity() {
    private lateinit var spiner: Spinner
    private lateinit var spinerPoslovnice:Spinner
    private lateinit var unosEmail: EditText
    private lateinit var unosLozinka: EditText
    private lateinit var btnDodaj: Button
    private lateinit var toolbar: MaterialToolbar
    private lateinit var addUserEmailTextInput: TextInputLayout
    private lateinit var addUserPasswordTextInput: TextInputLayout
    var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        toolbar = findViewById(R.id.addUserToolbar)
        spiner = findViewById(R.id.spiner)
        spinerPoslovnice = findViewById(R.id.spinnerPoslovnice)
        unosEmail = findViewById(R.id.unosEmail)
        unosLozinka = findViewById(R.id.unosLozinka)
        btnDodaj = findViewById(R.id.btnDodajKorisnik)
        addUserEmailTextInput = findViewById(R.id.addUserEmailTextInput)
        addUserPasswordTextInput = findViewById(R.id.addUserPasswordTextInput)


        val userID: String? = intent.getStringExtra("userID")

        btnDodaj.isEnabled = false

        if(userID != null) {
            Log.d("users", "userID nije null, ažuriranje usera")
            initializeUserData(userID)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnDodaj.setOnClickListener {
            onAddUser(userID)
        }

        unosEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addUserEmailTextInput.isErrorEnabled = false
                addUserEmailTextInput.error = null
                checkButtonState(userID)
            }
        })

        unosLozinka.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState(userID)
            }
        })

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, mutableListOf<String>())
        spinerPoslovnice.adapter = adapter
        BranchesService.getBranches {
            val poslovnice = mutableListOf<String>()
            BranchesService.branches.forEach { branch -> poslovnice.add(branch.nazivPoslovnice.toString()) }
            adapter.clear()
            adapter.addAll(poslovnice)
            adapter.notifyDataSetChanged()
            if(userID != null) {
                initializeSpinerPoslovnica(userID)
            }
        }




        spinerPoslovnice?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }
    }

    private fun onAddUser(userID: String?) {
        val admin: Boolean = spiner.selectedItem.toString().equals("Admin")

        var poslovnicaKorisnik = spinerPoslovnice.selectedItem.toString();
        //DODAVANJE KORISNIKA
        if(userID == null) {

            LoginService.checkIfEmailExists(unosEmail.text.toString()) {result ->
                if(result)
                {
                    addUserEmailTextInput.isErrorEnabled = true
                    addUserEmailTextInput.error = "Email se već koristi"
                } else if(admin){
                    UserService.createUser(
                        unosEmail.text.toString(),
                        unosLozinka.text.toString(),
                        admin,
                        ""
                    ){result ->
                        if(result) {
                            val output = Intent().apply {
                                putExtra("mode", "ADD")
                            }
                            setResult(Activity.RESULT_OK, output)
                            finish()
                        }
                    }
                } else if(!admin) {
                    UserService.createUser(
                        unosEmail.text.toString(),
                        unosLozinka.text.toString(),
                        admin,
                        poslovnicaKorisnik
                    ){result ->
                        if(result) {
                            val output = Intent().apply {
                                putExtra("mode", "ADD")
                            }
                            setResult(Activity.RESULT_OK, output)
                            finish()
                        }
                    }
                }
                }
        }

        //AŽURIRANJE KORISNIKA
        else {
            UserService.updateUser(
                userID,
                unosLozinka.text.toString(),
                admin,
                spinerPoslovnice.selectedItem as String
            ) {result ->
                if(result) {
                    val output = Intent().apply {
                        putExtra("mode", "EDIT")
                    }
                    setResult(Activity.RESULT_OK, output)
                    finish()
                }
            }
        }
    }

    private fun checkButtonState(userID: String?) {
        var emailCondition = userID != null || emailPattern.matcher(unosEmail.text).matches()
        var passwordCondition = unosLozinka.text.length >= 8
        btnDodaj.isEnabled = emailCondition && passwordCondition
    }

    private fun initializeSpinerPoslovnica(userID: String) {
        val user: Korisnik = UserService.users.firstOrNull { user -> user.getID() == userID } ?: return
        Log.d("users", "Pronađen user sa id = ${userID}")
        spinerPoslovnice.setSelection(BranchesService.branches.indexOfFirst { branch -> branch.nazivPoslovnice == user.poslovnica() })
    }

    private fun initializeUserData(userID: String) {
        val user: Korisnik = UserService.users.firstOrNull { user -> user.getID() == userID } ?: return
        Log.d("users", "Pronađen user sa id = ${userID}")

        unosEmail.setText(user.getEmail())
        unosEmail.isEnabled = false

        unosLozinka.setText(user.getPassword())

        Log.d("users", user.isAdmin().toString())

        if(user.isAdmin()) {
            spiner.setSelection(1)
        }
        else {
            spiner.setSelection(0)
        }

        toolbar.title="Ažuriraj korisnika"
        btnDodaj.text="SAČUVAJ"
        btnDodaj.isEnabled = true


        //Log.d("nesto", spinerPoslovnice.selectedItem.toString())
        /*for(x in 0..8) {
            if (user.poslovnica().equals(spinerPoslovnice.count)) {
                Log.d("oki1", user.poslovnica())
                Log.d("oki2", spinerPoslovnice.getItemAtPosition(x).toString())

                spinerPoslovnice.setSelection(x);
                break;
            }
        }*/
        /*
        val poslovnice2 = mutableListOf<String>("Sarajevo")
        var broj=0
        BranchesService.getBranches {
            BranchesService.branches.forEach { branch -> if(branch.nazivPoslovnice != "Sarajevo") poslovnice2.add(branch.nazivPoslovnice.toString()) }
            for(x in 0..poslovnice2.size) {
                if (user.poslovnica().equals(spinerPoslovnice.getItemAtPosition(x))) {
                    Log.d("oki1", user.poslovnica())
                    Log.d("oki2", spinerPoslovnice.getItemAtPosition(x).toString())

                    spinerPoslovnice.setSelection(x);
                    break;
                }
            }
        }*/



    }

}