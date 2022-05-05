package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import ba.etf.nrsprojekat.services.LoginService
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var passwordField: TextInputEditText
    private lateinit var confirmPasswordField: TextInputEditText
    private lateinit var confirmSaveDugme: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        passwordField = findViewById(R.id.changePassword1Field)
        confirmPasswordField = findViewById(R.id.changePassword2Field)
        confirmSaveDugme = findViewById(R.id.changePasswordSaveDugme)

        confirmSaveDugme.isEnabled = false

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButtonState()
            }
        })

        confirmPasswordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButtonState()
            }
        })

        confirmSaveDugme.setOnClickListener {
            onConfirm()
        }

    }

    private fun checkSaveButtonState() {
        var passwordCondition = passwordField.text?.isNotEmpty() ?: false && passwordField.text?.length!! >= 8
        var confirmPasswordCondition =  confirmPasswordField.text?.isNotEmpty() ?: false && confirmPasswordField.text.toString() == passwordField.text.toString()
        confirmSaveDugme.isEnabled = passwordCondition && confirmPasswordCondition
    }

    private fun onConfirm() {
        LoginService.changePassword(passwordField.text.toString()) {
            result ->
            if(result) {
                finish()
            }
        }
    }
}