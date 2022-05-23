package ba.etf.nrsprojekat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ba.etf.nrsprojekat.services.BranchesService
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.appbar.MaterialToolbar

class AddBranchActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nazivPoslovnice: TextView
    private lateinit var btnDodajPoslovnicu : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_branch)
        toolbar = findViewById(R.id.addBranchToolbar)
        nazivPoslovnice = findViewById(R.id.unosPoslovnice)
        btnDodajPoslovnicu = findViewById(R.id.btnDodajPoslovnicu)
        val productID: String? = intent.getStringExtra("productID")

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
        btnDodajPoslovnicu.setOnClickListener {
            BranchesService.addBranch(productID ?: "",nazivPoslovnice.text.toString(),){
                    result, mode ->
                if(result) {
                    val output = Intent().apply {
                        putExtra("branches", mode)
                    }
                    setResult(Activity.RESULT_OK, output)
                    finish()
                }
            }
        }
    }
    private fun onToolbarBackButton() {
        finish()
    }
}