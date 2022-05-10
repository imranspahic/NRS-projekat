package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
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
        val test = intent.getStringExtra("Bool")
        // validirati polja
        toolbar = findViewById(R.id.addUserToolbar)
        spiner = findViewById(R.id.spiner)
        unosEmail = findViewById(R.id.unosEmail)
        unosLozinka = findViewById(R.id.unosLozinka)
        btnDodaj = findViewById(R.id.btnDodajKorisnik)
        addUserEmailTextInput = findViewById(R.id.addUserEmailTextInput)
        addUserPasswordTextInput = findViewById(R.id.addUserPasswordTextInput)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        if(test.equals("false")) {
            btnDodaj.setOnClickListener {
                addUserEmailTextInput.isErrorEnabled=false
                addUserPasswordTextInput.isErrorEnabled=false
                if (!checkEmail()) {
                    addUserEmailTextInput.isErrorEnabled = true
                    addUserEmailTextInput.error = "Neispravan e-mail"
                }
                if(!checkPass()) {
                    addUserPasswordTextInput.isErrorEnabled = true
                    addUserPasswordTextInput.error = "Neispravna lozinka"
                }
                    else {
                    val admin: Boolean
                    if (spiner.selectedItem.toString().equals("Admin")) admin = true else admin = false
                    // UserService.dodajUBazu(
                    LoginService.createUser(
                        unosEmail.text.toString(),
                        unosLozinka.text.toString(),
                        admin
                    )  //it ->
                    //   if(it) Toast.makeText(this, "Korisnik dodan!", Toast.LENGTH_SHORT).show()
                }


            }
         //   Toast.makeText(applicationContext, "TOAAAST", Toast.LENGTH_LONG).show()
        }
        else if(test.equals("true")) {
            toolbar.title="Ažuriraj korisnika"
            btnDodaj.text="SAČUVAJ"
            val id = intent.getStringExtra("userID")
            if (id != null) {
                UserService.dajKorisnika(id) { rez ->
                    unosEmail.setText(rez.getEmail())
                    unosLozinka.setText(rez.getPassword())
                }
            }
            btnDodaj.setOnClickListener {
              //  spiner.getItemAtPosition(0)
                if(!checkPass()) {
                    addUserPasswordTextInput.isErrorEnabled = true
                    addUserPasswordTextInput.error = "Neispravna lozinka"
                }
                else {
                    UserService.izmijeniSifru(id.toString(), unosLozinka.text.toString()) {}
                    if (spiner.selectedItem.toString().equals("Admin")) UserService.izmijeniStatus(
                        id.toString(),
                        true
                    ) {}
                }

            }
        }
    }

    private fun checkEmail(): Boolean {
        if(unosEmail.text.isNotEmpty() && emailPattern.matcher(unosEmail.text).matches()) return true
        return false
    }
    private fun checkPass(): Boolean {
        if(unosLozinka.text.isNotEmpty() && unosLozinka.text.length >= 8) return true
        return false
    }
}