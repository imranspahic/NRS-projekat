package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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

        val productID: String? = intent.getStringExtra("productID")

        addProductSaveDugme.isEnabled = false

        if(productID != null) {
            Log.d("products", "productID nije null, ažuriranje proizvoda")
            initializeProductData(productID)
        }

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        addProductSaveDugme.setOnClickListener {
            onAddProduct(productID)
        }


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

    private fun onAddProduct(productID: String?) {
        ProductsService.addProduct(
            productID ?: "",
            addProductNameField.text.toString(),
            addProductPoslovnicaSpinner.selectedItem as String,
             addProductQuantityField.text.toString().toInt(),
            (addProductStatusSpinner.selectedItem as String).lowercase(),
        ) {
            result, mode ->
            if(result) {
                val output = Intent().apply {
                    putExtra("mode", mode)
                }
                setResult(Activity.RESULT_OK, output)
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

    private fun initializeProductData(productID: String) {
        val product: Product = ProductsService.products.firstOrNull { product -> product.id == productID } ?: return
        Log.d("products", "Pronađen proizvod sa id = ${productID}")

        addProductNameField.setText(product.name)
        addProductQuantityField.setText(product.quantity.toString())
        Log.d("products", product.status)
        when (product.status.lowercase()) {
            "dostupno" ->
                addProductStatusSpinner.setSelection(0)
            "nema na stanju" -> addProductStatusSpinner.setSelection(1)
            else -> {
                addProductStatusSpinner.setSelection(2)
            }
        }
        toolbar.title = "Ažuriraj proizvod"
        addProductSaveDugme.text = "SAČUVAJ"
        addProductSaveDugme.isEnabled = true

        //POVEZATI I addProductPoslovnicaSpinner sa produt.poslovnicaName
        //Trenutno su dummy vrijednosti u spinneru
    }

}