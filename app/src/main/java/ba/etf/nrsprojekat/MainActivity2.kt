package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ba.etf.nrsprojekat.view.FragmentAdmin
import ba.etf.nrsprojekat.view.FragmentOpcijeAdmin
import ba.etf.nrsprojekat.view.FragmentProducts
import ba.etf.nrsprojekat.view.FragmentProfile
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity2 : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var topNavigation: MaterialToolbar
    private val mOnItemSelectedListener = NavigationBarView.OnItemSelectedListener{ item ->
        when (item.itemId) {
            R.id.admin -> {
                val adminFragment = FragmentAdmin()
                openFragment(adminFragment)
                return@OnItemSelectedListener true
            }
            R.id.proizvodi -> {
                val productsFragment = FragmentProducts()
                openFragment(productsFragment)
                return@OnItemSelectedListener true
            }
            R.id.profil -> {
                val profileFragment = FragmentProfile()
                openFragment(profileFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        bottomNavigation = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener(mOnItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.admin

       // val adminFragment = FragmentAdmin()
       // openFragment(adminFragment)
        val opcijeFragment = FragmentOpcijeAdmin()
        openFragment(opcijeFragment)
    }

    public fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}