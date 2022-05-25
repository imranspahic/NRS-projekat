package ba.etf.nrsprojekat

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class EditBranchActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var nazivPoslovnice: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_branch)
        toolbar = findViewById(R.id.editBranchToolbar)
        nazivPoslovnice = findViewById(R.id.editPoslovnice)

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
    }
    private fun onToolbarBackButton() {
        finish()
    }

}