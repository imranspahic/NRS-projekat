package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class AddUserActivity : AppCompatActivity() {
    private lateinit var spiner: Spinner
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
    }

    private fun onAddUser(userID: String?) {
        val admin: Boolean = spiner.selectedItem.toString().equals("Admin")

        //DODAVANJE KORISNIKA
        if(userID == null) {

            LoginService.checkIfEmailExists(unosEmail.text.toString()) {result ->
                if(result)
                {
                    addUserEmailTextInput.isErrorEnabled = true
                    addUserEmailTextInput.error = "Email se već koristi"
                } else {
                    UserService.createUser(
                        unosEmail.text.toString(),
                        unosLozinka.text.toString(),
                        admin
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
                admin
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
    }

}