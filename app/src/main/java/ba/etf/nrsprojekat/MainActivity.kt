package ba.etf.nrsprojekat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import ba.etf.nrsprojekat.database.Firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var loginDugme: Button
    private lateinit var loginToggle: TextView
    private lateinit var linearLayout2: LinearLayout
    private lateinit var linearLayout3: LinearLayout



    private var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        confirmPasswordField = findViewById(R.id.confirmPasswordField)
        linearLayout2 = findViewById(R.id.linearLogin2)
        linearLayout3 = findViewById(R.id.linearLogin3)
        loginDugme = findViewById(R.id.loginDugme)
        loginToggle = findViewById(R.id.loginToggle)

        linearLayout3.visibility = View.GONE
        loginDugme.isEnabled = false
        loginToggle.setOnClickListener {
            toggleLoginState()
        }

        emailField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkLoginButtonState()
            }
        })

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkLoginButtonState()
            }
        })


        confirmPasswordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkLoginButtonState()
            }
        })

        loginDugme.setOnClickListener {
            Firestore.checkIfEmailExists(emailField.text.toString()) {emailExists ->
                Log.d("login", "checkUser() finished")
                if(emailExists) {
                    //Login
                    if(!linearLayout3.isVisible) {
                        Firestore.checkIfPasswordCorrect(emailField.text.toString(), passwordField.text.toString()){
                            success ->
                            if(success) onSuccessLogin()
                            else Log.d("login", "neispravna lozinka")
                        }
                    }
                    //Registracija
                    else {
                        Log.d("login", "error email se već koristi")
                    }
                }
                else {
                    //Login
                    if(!linearLayout3.isVisible) {
                        Log.d("login", "error email ne postoji")
                    }
                    //Registracija
                    else {
                        Firestore.createUser(emailField.text.toString(), passwordField.text.toString())
                        onSuccessLogin()
                    }
                }
            }
        }
    }

    private fun checkLoginButtonState() {
        var emailCondition = emailField.text.isNotEmpty() && emailPattern.matcher(emailField.text).matches()
        var passwordCondition = passwordField.text.isNotEmpty() && passwordField.text.length >=8
        var confirmPasswordCondition = !linearLayout3.isVisible || (confirmPasswordField.text.isNotEmpty() && confirmPasswordField.text.toString() == passwordField.text.toString())
        loginDugme.isEnabled = emailCondition && passwordCondition && confirmPasswordCondition
    }

    private fun onSuccessLogin() {

    }

    private fun toggleLoginState() {
        if(linearLayout3.isVisible) {
            loginToggle.text = "Nemate račun? Registrirajte se"
            loginDugme.text = "PRIJAVI SE"
            linearLayout3.visibility = View.GONE
            linearLayout2.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomToTop = loginDugme.id
            }
            loginDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = linearLayout2.id
            }
        }
        else {
            loginToggle.text = "Već imate račun? Prijavite se"
            loginDugme.text = "REGISTRIRAJ SE"
            linearLayout3.visibility = View.VISIBLE
            linearLayout2.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomToTop = linearLayout3.id
            }
            loginDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = linearLayout3.id
            }
        }
        checkLoginButtonState()
    }
}