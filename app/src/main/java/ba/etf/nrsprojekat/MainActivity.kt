package ba.etf.nrsprojekat

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.services.LoginService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var loginDugme: Button
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var passwordTextInputLayout: TextInputLayout

    private var emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-zA-Z]{2,4}")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout)
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout)
        loginDugme = findViewById(R.id.loginDugme)
        loginDugme.isEnabled = false

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

        loginDugme.setOnClickListener {
            LoginService.checkIfEmailExists(emailField.text.toString()) { emailExists ->
                Log.d("login", "checkUser() finished")
                if(emailExists) {
                        LoginService.checkIfPasswordCorrect(emailField.text.toString(), passwordField.text.toString()){
                            success ->
                            if(success) {
                                onSuccessLogin()
                            }

                            else {
                                passwordTextInputLayout.isErrorEnabled = true
                                passwordTextInputLayout.error = "Neispravna lozinka"
                                Log.d("login", "neispravna lozinka")
                            }
                        }
                }
                else {
                        emailTextInputLayout.isErrorEnabled = true
                        emailTextInputLayout.error = "Email se ne koristi"
                        Log.d("login", "error email ne postoji")
                }
            }
        }
    }

    private fun checkLoginButtonState() {
        var emailCondition = emailField.text?.isNotEmpty() ?: false && emailPattern.matcher(emailField.text).matches()
        var passwordCondition = passwordField.text?.isNotEmpty() ?: false && passwordField.text?.length!! >= 8
        loginDugme.isEnabled = emailCondition && passwordCondition
    }

    private fun onSuccessLogin() {

        LoginService.checkIfAdmin(emailField.text.toString()) {
            it ->
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("Status", it.toString())
            startActivity(intent);
            finishActivity(10);
        }
    }
}
