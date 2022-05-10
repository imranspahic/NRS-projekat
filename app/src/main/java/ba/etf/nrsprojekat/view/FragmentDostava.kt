package ba.etf.nrsprojekat.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.models.Korisnik
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.firestore.FirebaseFirestore
import ba.etf.nrsprojekat.services.ProductsService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentDostava : Fragment(R.layout.dostava_fragment) {
    private lateinit var recyclerViewDostava: RecyclerView
    private lateinit var userArrayList: ArrayList<Korisnik>
    private lateinit var myAdapter: DostavaAdapter
    private lateinit var viewPager : ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tab_layout)
        val adapter = ViewPagerAdapter(parentFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager){ tab,position->
            when(position){
                0->{
                    tab.text = "Svi"
                }
                1->{
                    tab.text = "Primljeni"
                }
                2->{
                    tab.text = "Poslani"
                }
                3->{
                    tab.text = "Isporuceni"
                }
            }
        }.attach()

    }

}