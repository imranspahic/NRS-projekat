package ba.etf.nrsprojekat

import FragmentBranches
import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import ba.etf.nrsprojekat.services.LoginService
import ba.etf.nrsprojekat.view.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var topNavigation: MaterialToolbar

    private val mOnItemSelectedListener = NavigationBarView.OnItemSelectedListener{ item ->
        when (item.itemId) {
            R.id.narudzba -> {
                val narudzbaFragment = FragmentOrder()
                openFragment(narudzbaFragment)
                return@OnItemSelectedListener true
            }
            R.id.admin -> {
                val adminFragment = FragmentOpcijeAdmin()
                openFragment(adminFragment)
                return@OnItemSelectedListener true
            }
            R.id.proizvodi -> {
                val productsFragment = FragmentProducts()
                openFragment(productsFragment)
                return@OnItemSelectedListener true
            }

            R.id.poslovnice -> {
                val branchesFragment = FragmentBranches()
                openFragment(branchesFragment)
                return@OnItemSelectedListener true
            }
            R.id.profil -> {
                val profileFragment = FragmentProfile()
                openFragment(profileFragment)
                return@OnItemSelectedListener true
            }
            R.id.dostava -> {
                val deliveryFragment = FragmentDostava()
                openFragment(deliveryFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val status = intent.getStringExtra("Status")
        bottomNavigation = findViewById(R.id.bottom_nav)
        bottomNavigation.setOnItemSelectedListener(mOnItemSelectedListener)
        bottomNavigation.menu.clear()

        //ADMIN
        if(status.equals("true")) {
            bottomNavigation.inflateMenu(R.menu.admin_navigacija)
            val opcijeFragment = FragmentOpcijeAdmin()
            bottomNavigation.selectedItemId = R.id.admin
            openFragment(opcijeFragment)
        }

        //KORISNIK
        else {
            bottomNavigation.inflateMenu(R.menu.korisnik_navigacija)
            val orderFragment = FragmentOrder()
            bottomNavigation.selectedItemId = R.id.narudzba
            openFragment(orderFragment)
        }
    }

     fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}