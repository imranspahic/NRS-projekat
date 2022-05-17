package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import ba.etf.nrsprojekat.data.models.Product
import ba.etf.nrsprojekat.services.PdvCategoriesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import java.text.DecimalFormat


class AddProductActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    private lateinit var addProductNameField: TextInputEditText
    private lateinit var addProductQuantityField: TextInputEditText
    private lateinit var addProductPriceField: TextInputEditText
    private lateinit var addProductMjernaJedinicaSpinner: Spinner
    private lateinit var addProductPoslovnicaSpinner: Spinner
    private lateinit var addProductPdvSpinner: Spinner
    private lateinit var addProductStatusSpinner: Spinner
    private lateinit var addProductSaveDugme: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        toolbar = findViewById(R.id.addProductToolbar)
        addProductNameField = findViewById(R.id.addProductNameField)
        addProductQuantityField = findViewById(R.id.addProductQuantityField)
        addProductPriceField = findViewById(R.id.addProductPriceField)
        addProductMjernaJedinicaSpinner = findViewById(R.id.addProductMjernaJedinicaSpinner)
        addProductPoslovnicaSpinner = findViewById(R.id.addProductPoslovnicaSpinner)
        addProductPdvSpinner = findViewById(R.id.addProductPdvSpinner)
        addProductStatusSpinner = findViewById(R.id.addProductStatusSpinner)
        addProductSaveDugme = findViewById(R.id.addProductSaveDugme)

        val productID: String? = intent.getStringExtra("productID")

        addProductSaveDugme.isEnabled = false

        val pdvCategoryList = mutableListOf<String>("Nema kategorije")
        PdvCategoriesService.pdvCategories.forEach { category -> pdvCategoryList.add(category.toString()) }

        val pdvSpinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            pdvCategoryList
        )

        addProductPdvSpinner.adapter = pdvSpinnerAdapter

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

        addProductPriceField.addTextChangedListener(object : TextWatcher {
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
        var pdvCategoryName =
            if((addProductPdvSpinner.selectedItem as String) == "Nema kategorije") null
            else (addProductPdvSpinner.selectedItem as String).split(Regex(" \\("))[0].trim()
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_EVEN
        val roundedPrice = df.format(addProductPriceField.text.toString().replace(",", ".").toDouble())
        ProductsService.addProduct(
            productID ?: "",
            addProductNameField.text.toString(),
            addProductPoslovnicaSpinner.selectedItem as String,
            pdvCategoryName,
             addProductQuantityField.text.toString().toInt(),
            roundedPrice.replace(",", ".").toDouble(),
            (addProductStatusSpinner.selectedItem as String).lowercase(),
            (addProductMjernaJedinicaSpinner.selectedItem as String),
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
        var productPriceCondition =  addProductPriceField.text?.isNotEmpty() ?: false

        addProductSaveDugme.isEnabled =
            productNameCondition
                    && productQuantityCondition
                    && productPriceCondition
    }

    private fun initializeProductData(productID: String) {
        val product: Product = ProductsService.products.firstOrNull { product -> product.id == productID } ?: return
        Log.d("products", "Pronađen proizvod sa id = ${productID}")
        Log.d("products", "Pronađen proizvod sa pdvKategorijom = ${product.pdvCategoryName}")


        addProductNameField.setText(product.name)
        addProductQuantityField.setText(product.quantity.toString())
        addProductPriceField.setText(String.format("%.2f", product.price) )
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

        if(product.pdvCategoryName == null) {
            addProductPdvSpinner.setSelection(0)
        }
        else addProductPdvSpinner.setSelection(
            PdvCategoriesService.pdvCategories.indexOfFirst { category ->
                category.name == product.pdvCategoryName!! }+1
        )

        when (product.mjernaJedinica) {
            "kg" -> addProductMjernaJedinicaSpinner.setSelection(0)
            "g" -> addProductMjernaJedinicaSpinner.setSelection(1)
            "l" -> addProductMjernaJedinicaSpinner.setSelection(2)
            "ml" -> addProductMjernaJedinicaSpinner.setSelection(3)
            else -> {
                addProductMjernaJedinicaSpinner.setSelection(4)
            }
        }

        //POVEZATI I addProductPoslovnicaSpinner sa produt.poslovnicaName
        //Trenutno su dummy vrijednosti u spinneru
    }

}