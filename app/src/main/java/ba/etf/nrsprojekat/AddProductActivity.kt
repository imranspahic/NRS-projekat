package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar

class AddProductActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        toolbar = findViewById(R.id.addProductToolbar)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
    }

    private fun onToolbarBackButton() {
        finish()
    }
}