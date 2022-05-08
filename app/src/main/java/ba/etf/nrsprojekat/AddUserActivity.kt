package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import ba.etf.nrsprojekat.services.UserService
import com.google.android.material.button.MaterialButton
import java.util.regex.Pattern

class AddUserActivity : AppCompatActivity() {
    private lateinit var spiner: Spinner
    private lateinit var unosEmail: EditText
    private lateinit var unosLozinka: EditText
    private lateinit var btnDodaj: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        val test = intent.getStringExtra("Bool")
        // validirati polja
        spiner = findViewById(R.id.spiner)
        unosEmail = findViewById(R.id.unosEmail)
        unosLozinka = findViewById(R.id.unosLozinka)
        btnDodaj = findViewById(R.id.btnDodajKorisnik)
        var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")
        if(test.equals("false")) {
            btnDodaj.setOnClickListener {
                val admin: Boolean
                if (spiner.selectedItem.toString().equals("Admin")) admin = true else admin = false
                UserService.dodajUBazu(
                    unosEmail.text.toString(),
                    unosLozinka.text.toString(),
                    admin
                ) { //it ->
                    //   if(it) Toast.makeText(this, "Korisnik dodan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if(test.equals("true")) {
            val id = intent.getStringExtra("userID")
            println(id)
        }
    }
}