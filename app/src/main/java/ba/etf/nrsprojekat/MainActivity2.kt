package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val fragment = admin_user_change()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame, fragment)
            commit()
        }
    }
}