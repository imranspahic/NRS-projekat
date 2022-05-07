package ba.etf.nrsprojekat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Spinner
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddProductActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    private lateinit var addProductNameField: TextInputEditText
    private lateinit var addProductQuantityField: TextInputEditText
    private lateinit var addProductPoslovnicaSpinner: Spinner
    private lateinit var addProductStatusSpinner: Spinner
    private lateinit var addProductSaveDugme: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        toolbar = findViewById(R.id.addProductToolbar)
        addProductNameField = findViewById(R.id.addProductNameField)
        addProductQuantityField = findViewById(R.id.addProductQuantityField)
        addProductPoslovnicaSpinner = findViewById(R.id.addProductPoslovnicaSpinner)
        addProductStatusSpinner = findViewById(R.id.addProductStatusSpinner)
        addProductSaveDugme = findViewById(R.id.addProductSaveDugme)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        addProductSaveDugme.setOnClickListener {
            onAddProduct()
        }

        addProductSaveDugme.isEnabled = false

        addProductNameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkButtonState()
            }
        })

        addProductQuantityField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setProductStatus()
                checkButtonState()
            }
        })
    }

    private fun onToolbarBackButton() {
        finish()
    }

    private fun onAddProduct() {
        ProductsService.addProduct(
            "",
            addProductNameField.text.toString(),
            addProductPoslovnicaSpinner.selectedItem as String,
             addProductQuantityField.text.toString().toInt(),
            addProductStatusSpinner.selectedItem as String,
        ) {
            result ->
            if(result) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setProductStatus() {
        if(addProductQuantityField.text?.isEmpty() != false) return
        val quantity = addProductQuantityField.text.toString().toInt()
        if(quantity == 0) {
            addProductStatusSpinner.setSelection(1)
        }
        else {
            addProductStatusSpinner.setSelection(0)
        }
    }

    private fun checkButtonState() {
        var productNameCondition = addProductNameField.text?.isNotEmpty() ?: false
        var productQuantityCondition =  addProductQuantityField.text?.isNotEmpty() ?: false
        addProductSaveDugme.isEnabled = productNameCondition && productQuantityCondition
    }

}