package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ba.etf.nrsprojekat.R
import com.google.android.material.appbar.MaterialToolbar

class PdvCategoriesActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdv_categories)
        toolbar = findViewById(R.id.pdvCategoriesToolbar)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

    }

    private fun onToolbarBackButton() {
        finish()
    }

}