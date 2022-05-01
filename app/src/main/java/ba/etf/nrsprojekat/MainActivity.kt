package ba.etf.nrsprojekat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import ba.etf.nrsprojekat.database.Firestore
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var confirmPasswordField: TextInputEditText
    private lateinit var loginDugme: Button
    private lateinit var loginToggle: TextView

    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var confirmPasswordTextInputLayout: TextInputLayout



    private var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        confirmPasswordField = findViewById(R.id.confirmPasswordField)
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout)
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout)
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout)
        loginDugme = findViewById(R.id.loginDugme)
        loginToggle = findViewById(R.id.loginToggle)

        confirmPasswordTextInputLayout.visibility = View.GONE
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
                emailTextInputLayout.error = null
                passwordTextInputLayout.error = null
                emailTextInputLayout.isErrorEnabled = false
                passwordTextInputLayout.isErrorEnabled = false
                checkLoginButtonState()
            }
        })

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailTextInputLayout.error = null
                passwordTextInputLayout.error = null
                emailTextInputLayout.isErrorEnabled = false
                passwordTextInputLayout.isErrorEnabled = false
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
                    if(!confirmPasswordTextInputLayout.isVisible) {
                        Firestore.checkIfPasswordCorrect(emailField.text.toString(), passwordField.text.toString()){
                            success ->
                            if(success) onSuccessLogin()
                            else {
                                passwordTextInputLayout.isErrorEnabled = true
                                passwordTextInputLayout.error = "Neispravna lozinka"
                                Log.d("login", "neispravna lozinka")
                            }
                        }
                    }
                    //Registracija
                    else {
                        emailTextInputLayout.isErrorEnabled = true
                        emailTextInputLayout.error = "Email se već koristi"
                        Log.d("login", "error email se već koristi")
                    }
                }
                else {
                    //Login
                    if(!confirmPasswordTextInputLayout.isVisible) {
                        emailTextInputLayout.isErrorEnabled = true
                        emailTextInputLayout.error = "Email se ne koristi"
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
        var emailCondition = emailField.text?.isNotEmpty() ?: false && emailPattern.matcher(emailField.text).matches()
        var passwordCondition = passwordField.text?.isNotEmpty() ?: false && passwordField.text?.length!! >= 8
        var confirmPasswordCondition = !confirmPasswordTextInputLayout.isVisible || (confirmPasswordField.text?.isNotEmpty() ?: false && confirmPasswordField.text.toString() == passwordField.text.toString())
        loginDugme.isEnabled = emailCondition && passwordCondition && confirmPasswordCondition
    }

    private fun onSuccessLogin() {
        //prebaciti na sljedeci screen
        Toast.makeText(this@MainActivity, "Uspješno prijavljen korisnik", Toast.LENGTH_SHORT).show()
    }

    private fun toggleLoginState() {
        emailTextInputLayout.isErrorEnabled = false
        passwordTextInputLayout.isErrorEnabled = false
        if(confirmPasswordTextInputLayout.isVisible) {
            loginToggle.text = "Nemate račun? Registrirajte se"
            loginDugme.text = "PRIJAVI SE"
            confirmPasswordTextInputLayout.visibility = View.GONE
            passwordTextInputLayout.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomToTop = loginDugme.id
            }
            loginDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = passwordTextInputLayout.id
            }
        }
        else {
            loginToggle.text = "Već imate račun? Prijavite se"
            loginDugme.text = "REGISTRIRAJ SE"
            confirmPasswordTextInputLayout.visibility = View.VISIBLE
            passwordTextInputLayout.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomToTop = confirmPasswordTextInputLayout.id
            }
            loginDugme.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = confirmPasswordTextInputLayout.id
            }
        }
        checkLoginButtonState()
    }
}